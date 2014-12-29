package music.parser;

import game.settings.GameplayType;
import game.settings.GameplayTypeSetting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import util.Pair;
import music.GeneralInstrument;
import music.Handedness;
import music.Instrument;
import music.InstrumentPiece;
import music.InstrumentType;
import music.MidiVoice;
import music.Music;
import music.Note;
import music.Simultaneous;
import music.SoundElement;
import music.Voice;

public class MidiParser {
    private static final int MIDDLE_C = 60;
    private static final int SET_TEMPO = 0x51;
    private static final int TIME_SIGNATURE = 0x58;
    
    public static Music parse( String musicTitle, File file ) throws MidiUnavailableException {
        // There are 3 steps to this:
        //      1. put in the note messages into a sorted list (by tick time)
        //      2. put in notes (but not simultaneous) into a sorted list (by tick time)
        //      3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        //      4. compute time until voices start
        
        // step 0. sort every midievent by tick time.
        Pair<PriorityQueue<MidiEvent>,Integer> midiEventsAndResolution = sortedMidiEvents( file );
        PriorityQueue<MidiEvent> midiEvents = midiEventsAndResolution.getLeft();
        int ticksPerBeat = midiEventsAndResolution.getRight();
        
        // step 1. put in the note messages into a sorted list (by tick time)
        
        double beatsPerQuarterNote = 1;
        List<Pair<Long,Long>> timeAtTicks = new ArrayList<Pair<Long,Long>>();
        Map<Long,Long> tickToTime = new HashMap<Long,Long>();
        Map<Integer,LinkedList<NoteMessage>> programNumberToNoteMessages = new HashMap<Integer,LinkedList<NoteMessage>>();
        Map<Integer,Integer> channelToProgramNumber = new HashMap<Integer,Integer>();
        int currentProgramNumber = 1000; // dummy number for initial program number
        
        double millisecondsPerTick = 3.5; // some random number
        
        while ( ! midiEvents.isEmpty() ) {
            MidiEvent event = midiEvents.poll();
            long tick = event.getTick();
            
            MidiMessage message = event.getMessage();
            if ( message instanceof ShortMessage ) {
                ShortMessage sm = (ShortMessage) message;
                int command = sm.getCommand();
                if ( command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF ) {
                    if ( command == ShortMessage.NOTE_ON ) {
                        int channel = sm.getChannel();
                        int programNumber = 0; // default program number is 0 if channel doesn't exist.
                        if ( channelToProgramNumber.containsKey(channel) ) {
                            programNumber = channelToProgramNumber.get( channel );
                        } else {
                            System.out.println("They didn't even set the program number!");
                            channelToProgramNumber.put( channel, 0 );
                            programNumber = 0;
                        }
                        
                        if ( !programNumberToNoteMessages.containsKey(programNumber) ) {
                            programNumberToNoteMessages.put(programNumber, new LinkedList<NoteMessage>());
                        }
                        List<NoteMessage> noteMessages = programNumberToNoteMessages.get( programNumber );
                    
                        if ( sm.getData2() != 0 ) { 
                            noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), true ) );
                        } else {
                            noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), false ) );
                        }
                    } else if ( command == ShortMessage.NOTE_OFF ) {
                        int channel = sm.getChannel();
                        int programNumber = channelToProgramNumber.get( channel );
                        List<NoteMessage> noteMessages = programNumberToNoteMessages.get( programNumber );
                        noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), false ) );
                    }
                    
                    Pair<Long,Long> lastPair = timeAtTicks.get( timeAtTicks.size() - 1 );
                    long lastTick = lastPair.getLeft();
                    long lastTime = lastPair.getRight();
                    long currentTime = (long) (lastTime + ( tick - lastTick ) * millisecondsPerTick );
                    timeAtTicks.add( new Pair<Long,Long>( tick, currentTime ) );
                    tickToTime.put( tick, currentTime );
                    
                } else if ( command == ShortMessage.PROGRAM_CHANGE ) {
                    int channel = sm.getChannel();
                    currentProgramNumber = sm.getData1();
                    if ( !programNumberToNoteMessages.containsKey(currentProgramNumber) ) {
                        programNumberToNoteMessages.put( currentProgramNumber, new LinkedList<NoteMessage>() );
                    }
                    channelToProgramNumber.put( channel, currentProgramNumber );
                } else {
                    continue; // just ignore the message if it doesn't relate to the note or instrument
                }
            } else if ( message instanceof MetaMessage ){
                MetaMessage mm = (MetaMessage) message;
                int type = mm.getType();
                if ( type == SET_TEMPO ) {
                    // assume that tempo change is going to occur before notes
                    if ( timeAtTicks.isEmpty() ) {
                        timeAtTicks.add( new Pair<Long,Long>( tick, 0L ) );
                    } else {
                        Pair<Long,Long> lastPair = timeAtTicks.get( timeAtTicks.size() - 1 );
                        long lastTick = lastPair.getLeft();
                        long lastTime = lastPair.getRight();
                        long currentTime = (long) (lastTime + ( tick - lastTick ) * millisecondsPerTick );
                        timeAtTicks.add( new Pair<Long,Long>( tick, currentTime ) );
                        tickToTime.put( tick, currentTime );
                    }    
                    byte[] bytes = mm.getMessage();
                    int MPQNByte1 = bytes[3] & 0xFF;
                    int MPQNByte2 = bytes[4] & 0xFF;
                    int MPQNByte3 = bytes[5] & 0xFF;
                    
                    int microsecondsPerQuarterNote = MPQNByte1 * 0xFF * 0xFF + MPQNByte2 * 0xFF + MPQNByte3; 
                    millisecondsPerTick = microsecondsPerQuarterNote / (ticksPerBeat * beatsPerQuarterNote * 1000.0);
                } else if ( type == TIME_SIGNATURE ) {
                    byte[] bytes = mm.getMessage();
                    beatsPerQuarterNote *= 1;//(bytes[4] & 0xFF) / 4.0;
                    System.out.println("Time Signature: " + (bytes[3] & 0xFF) + "/" + (bytes[4] & 0xFF) + ", Met: " + (bytes[5] & 0xFF) );
                }
            }
        }
        
        // Step 1.5: sort the time at ticks by the tick.
        Collections.sort(timeAtTicks, new Comparator<Pair<Long,Long>>() {
            @Override
            public int compare(Pair<Long, Long> pair, Pair<Long, Long> otherPair) {
                // TODO Auto-generated method stub
                return (int) (pair.getLeft() - otherPair.getLeft());
            }
        });
        
        
        // step 2. put in notes (but not simultaneous) into a sorted list (by tick time)
        
        Map<Integer,Map<Handedness,LinkedList<Pair<Long,Note>>>> programNumberTo_HandToNotes = new HashMap<Integer,Map<Handedness,LinkedList<Pair<Long,Note>>>>();
        for ( int programNumber : programNumberToNoteMessages.keySet() ) {
            LinkedList<NoteMessage> noteMessages = programNumberToNoteMessages.get(programNumber);
            Collections.sort(noteMessages);
            
            Map<Handedness,LinkedList<Pair<Long,Note>>> handToNotes = new HashMap<Handedness,LinkedList<Pair<Long,Note>>>();
            
            while ( !noteMessages.isEmpty() ) {
                NoteMessage noteMessage = noteMessages.remove();
                int value = noteMessage.value();
                Handedness handedness = handedness( programNumber, value );
                int volume = noteMessage.volume();
                long tick = noteMessage.tick();
                int i = 0;
                NoteMessage nextNoteOffMessage = null;
                if ( i != noteMessages.size() ) {
                    nextNoteOffMessage = noteMessages.get(i);
                } else {
                    nextNoteOffMessage = new NoteMessage( tick, value, volume, false );
                }
                while ( nextNoteOffMessage.on() || nextNoteOffMessage.value() != value ) {
                    i += 1;
                    if ( i == noteMessages.size() ) {
                        System.out.println("Yea, something went wrong here... Does not exist note off to pair with note on!");
                        break;
                    }
                    nextNoteOffMessage = noteMessages.get(i);
                }
                if ( i != noteMessages.size() ) {
                    NoteMessage noteOffMessage = noteMessages.remove(i);
                    long offTick = noteOffMessage.tick();
                    int duration = (int) ( (tickToTime.get(offTick) - tickToTime.get(tick)) );
                    Note note = new Note( value, duration, volume);
                    note.setTick(tick);
                    
                    if ( handToNotes.containsKey(handedness) ) {
                        LinkedList<Pair<Long,Note>> notes = handToNotes.get(handedness);
                        notes.add( new Pair<Long,Note>( tick, note ) );
                    } else {
                        LinkedList<Pair<Long,Note>> notes = new LinkedList<Pair<Long,Note>>();
                        notes.add( new Pair<Long,Note>( tick, note ) );
                        handToNotes.put( handedness, notes );
                    }
                } else {
                    int duration = 0;
                    Note note = new Note( value, duration, volume);
                    note.setTick(tick);
                    
                    if ( handToNotes.containsKey(handedness) ) {
                        LinkedList<Pair<Long,Note>> notes = handToNotes.get(handedness);
                        notes.add( new Pair<Long,Note>( tick, note ) );
                    } else {
                        LinkedList<Pair<Long,Note>> notes = new LinkedList<Pair<Long,Note>>();
                        notes.add( new Pair<Long,Note>( tick, note ) );
                        handToNotes.put( handedness, notes );
                    }
                }
            }
            programNumberTo_HandToNotes.put( programNumber, handToNotes );
        }
        
        // Step X: set program number to instruments
        Map<Integer,Instrument> programNumberToInstrument = new HashMap<Integer,Instrument>();
        int numberOfProgramsLeft = programNumberTo_HandToNotes.keySet().size();
        int numberOfChannelsLeft = 15;
        int channelIdx = 0;
        int[] availableChannels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15 };
        for ( int programNumber : programNumberToNoteMessages.keySet() ) {
            int numberOfChannelsPickedOff = (int) Math.ceil( ((double) numberOfChannelsLeft) / numberOfProgramsLeft );
            numberOfProgramsLeft--;
            numberOfChannelsLeft -= numberOfChannelsPickedOff;
            int[] channelsToUse = new int[numberOfChannelsPickedOff];
            for ( int j=0; j < numberOfChannelsPickedOff; j++ ) {
                channelsToUse[j] = availableChannels[channelIdx];
                channelIdx++;
            }
            
            programNumberToInstrument.put( programNumber, new GeneralInstrument( programNumber, channelsToUse ) );
        }
        
        // step 3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        
        Map<Integer,Map<Handedness,Integer>> programNumberTo_HandToTimeUntilVoiceStarts = new HashMap<Integer,Map<Handedness,Integer>>();
        Map<Integer,Map<Handedness,Voice>> programNumberTo_HandToVoice = new HashMap<Integer,Map<Handedness,Voice>>();
        
        for ( int programNumber : programNumberTo_HandToNotes.keySet() ) {
            Map<Handedness,Long> handToFirstTick = new HashMap<Handedness,Long>();
            Map<Handedness,Voice> handToVoice = new HashMap<Handedness,Voice>();
            Map<Handedness,Integer> handToTimeUntilVoiceStarts = new HashMap<Handedness,Integer>();
            Map<Handedness,LinkedList<Pair<Long,Note>>> handToNotes = programNumberTo_HandToNotes.get(programNumber);
            
            for ( Handedness handedness : handToNotes.keySet() ) {
                int i = 0;
                long oldTick = 0; // arbitrary starting value with no meaning
                List<Integer> timesUntilNextElement = new ArrayList<Integer>();
                List<SoundElement> soundElements = new ArrayList<SoundElement>();
                LinkedList<Pair<Long,Note>> notes = handToNotes.get(handedness);
                while ( !notes.isEmpty() ) {
                    List<Note> simultaneousNotes = new ArrayList<Note>();
                    Pair<Long,Note> pair = notes.remove();
                    long tick = pair.getLeft();
                    if ( i != 0 ) {
                        timesUntilNextElement.add( (int) (tickToTime.get(tick) - tickToTime.get(oldTick)) );
                    } else {
                        handToFirstTick.put(handedness,tick);
                    }
                    Note note = pair.getRight();
                    simultaneousNotes.add(note);
                    while ( !notes.isEmpty() ) {
                        Pair<Long,Note> nextPair = notes.peek();
                        if ( nextPair.getLeft() == tick ) {
                            notes.remove();
                            Note simultaneousNote = nextPair.getRight();
                            simultaneousNotes.add( simultaneousNote );
                        } else {
                            break;
                        }
                    }
                    if ( simultaneousNotes.size() == 1 ) {
                        soundElements.add(simultaneousNotes.get(0));
                    } else if ( simultaneousNotes.size() > 1 ) {
                        soundElements.add( new Simultaneous(simultaneousNotes) );
                    } else {
                        throw new RuntimeException("Something is wrong here...");
                    }
                    
                    oldTick = tick;
                    
                    i++;
                }
                
                Instrument instrument = programNumberToInstrument.get(programNumber);
                Voice voice = new MidiVoice( soundElements, timesUntilNextElement, handedness, instrument );
                handToVoice.put(handedness, voice);
            //}
            
            
            // step 4. compute time until voices start
            
            //Map<Integer,Integer> programNumberToTimeUntilVoiceStarts = new HashMap<Integer,Integer>();
            //for ( int programNumber : firstTicks.keySet() ) {
               long tick = handToFirstTick.get(handedness);
               int timeUntilVoiceStarts = 0;
               long timeAtTick = tickToTime.get(tick);
               timeUntilVoiceStarts = (int) timeAtTick;
               
               handToTimeUntilVoiceStarts.put(handedness, timeUntilVoiceStarts);
               
               
            }
            programNumberTo_HandToTimeUntilVoiceStarts.put(programNumber,handToTimeUntilVoiceStarts);
            programNumberTo_HandToVoice.put(programNumber, handToVoice);
        }
        
        List<InstrumentPiece> instrumentPieces = new ArrayList<InstrumentPiece>();
        
        for ( int programNumber : programNumberTo_HandToTimeUntilVoiceStarts.keySet() ) {
            Map<Handedness,Integer> handToTimeUntilVoiceStarts = programNumberTo_HandToTimeUntilVoiceStarts.get(programNumber);
            Map<Handedness,Voice> handToVoice = programNumberTo_HandToVoice.get(programNumber);
            List<Voice> voices = new ArrayList<Voice>();
            List<Integer> timesUntilVoicesStart = new ArrayList<Integer>();
            for ( Handedness handedness : handToVoice.keySet() ) {
                Voice voice = handToVoice.get(handedness);
                voices.add(voice);
                
                if ( handToTimeUntilVoiceStarts.containsKey(handedness) ) {
                    timesUntilVoicesStart.add( handToTimeUntilVoiceStarts.get( handedness ) );
                } else {
                    timesUntilVoicesStart.add( 0 );
                }
            }
            
            Instrument instrument = programNumberToInstrument.get(programNumber);
            
            InstrumentPiece instrumentPiece = new InstrumentPiece( voices, timesUntilVoicesStart, instrument );
            instrumentPieces.add(instrumentPiece);
        }
        
        return new Music( musicTitle, instrumentPieces );
    }
    
    private static Pair<PriorityQueue<MidiEvent>,Integer> sortedMidiEvents( File file ) {
        PriorityQueue<MidiEvent> midiEvents = new PriorityQueue<MidiEvent>(new Comparator<MidiEvent>() {
            @Override
            public int compare(MidiEvent event, MidiEvent otherEvent) {
                long eventTick = event.getTick();
                long otherEventTick = otherEvent.getTick();
                int tickDifference = (int) (eventTick - otherEventTick);
                if ( tickDifference != 0 ) {
                    return tickDifference;
                } else {
                    MidiMessage message = event.getMessage();
                    MidiMessage otherMessage = event.getMessage();
                    // message is meta message
                    if ( message instanceof MetaMessage ) {
                        return -1;
                    // both are short message
                    } else if ( otherMessage instanceof ShortMessage ) {
                        ShortMessage shortMessage = (ShortMessage) message;
                        if ( shortMessage.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
                            return -1;
                        } else {
                            return 1;
                        }
                    // both are meta message;
                    } else {
                        return 1;
                    }
                }
            }
        });
        int ticksPerBeat = 0;
        try {
            Sequence sequence = MidiSystem.getSequence( file );
            ticksPerBeat = sequence.getResolution();
            
            for (Track track : sequence.getTracks()) {
                for ( int i=0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    midiEvents.add(event);
                }
            }
        } catch ( IOException | InvalidMidiDataException e ) {
            e.printStackTrace();
        }
        
        return new Pair<PriorityQueue<MidiEvent>,Integer>(midiEvents,ticksPerBeat);
    }
    
    private static Handedness handedness( int programNumber, int value ) {
        if ( GameplayTypeSetting.gameplayType() == GameplayType.ONE_HANDED ) {
            return Handedness.SINGLE;
        } else {
            if ( Instrument.typeOfInstrument( programNumber ) == InstrumentType.SINGLE ) {
                return Handedness.SINGLE;
            } else {
                if ( value > MIDDLE_C ) {
                    return Handedness.LEFT;
                } else {
                    return Handedness.RIGHT;
                }
            }
        }
    }
    
    /**
     * Found here: http://stackoverflow.com/questions/3850688/reading-midi-files-in-java
     * Written by Sami Koivu
     * @throws Exception
     */
    public static void analyze() throws Exception {
        final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
        final int NOTE_ON = 0x90;
        final int NOTE_OFF = 0x80;
        Sequence sequence = MidiSystem.getSequence(new File("res/midi/Butterfly.mid"));
        System.out.println("Division Type: " + sequence.getDivisionType());
        
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                if ( event.getTick() > 40000 ) { continue; }
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if ( sm.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
                        System.out.println("Program Change, " + sm.getData1());
                    } else {
                        System.out.println("Command: " + sm.getCommand());
                    }
                } else if ( message instanceof MetaMessage ) {
                    MetaMessage mm = (MetaMessage) message;
                    System.out.println("Type of MetaMessage: " + mm.getType());
                } else {
                    System.out.println("Here");
                }
            }

            System.out.println();
        }
    }
    
    public static void main(String[] args) throws Exception {
        analyze();
    }
}
