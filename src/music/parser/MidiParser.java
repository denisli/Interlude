package music.parser;

import game.InstrumentType;

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
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

import util.Pair;
import util.Triple;
import music.GeneralInstrument;
import music.Handedness;
import music.MidiVoice;
import music.Music;
import music.MusicElement;
import music.Note;
import music.Rest;
import music.Simultaneous;
import music.Voice;

public class MidiParser {
    private static final int SET_TEMPO = 0x51;
    private static final int TIME_SIGNATURE = 0x58;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
    public static Music parse(File file, int splittingOctave) throws MidiUnavailableException {
        // There are 3 steps to this:
        //      1. put in the note messages into a sorted list (by tick time)
        //      2. put in notes (but not simultaneous) into a sorted list (by tick time)
        //      3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        //      4. compute time until voices start
        
        // step 0. sort every midievent by tick time.
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
        
        // step 1. put in the note messages into a sorted list (by tick time)
        double beatsPerQuarterNote = 1;//0;
        List<Pair<Long,Double>> millisecondsPerTickAtTick = new ArrayList<Pair<Long,Double>>();
        Map<Integer,LinkedList<NoteMessage>> programNumberToNoteMessages = new HashMap<Integer,LinkedList<NoteMessage>>();
        Map<Integer,Integer> channelToProgramNumber = new HashMap<Integer,Integer>();
        int currentProgramNumber = 1000; // dummy number for initial program number
        
        double millisecondsPerTick = 500.0 / 24;
        
        while ( ! midiEvents.isEmpty() ) {
                    MidiEvent event = midiEvents.poll();
                    long tick = event.getTick();
                    
                    MidiMessage message = event.getMessage();
                    if ( message instanceof ShortMessage ) {
                        ShortMessage sm = (ShortMessage) message;
                        int command = sm.getCommand();
                        if ( command == ShortMessage.NOTE_ON ) {
                            int channel = sm.getChannel();
                            int programNumber = 0; // default program number is 0 if channel doesn't exist.
                            if ( channelToProgramNumber.containsKey(channel) ) {
                                programNumber = channelToProgramNumber.get( channel );
                            }
                            //System.out.println("Program Number: " + programNumber + ", Channel: " + channel);
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( programNumber );
                            
                            if ( sm.getData2() != 0 ) { 
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), true ) );
                            } else {
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), false ) );
                            }
                        } else if ( command == ShortMessage.NOTE_OFF ) {
                            int channel = sm.getChannel();
                            int programNumber = channelToProgramNumber.get( channel );
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( programNumber );
                            noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), false ) );
                        } else if ( command == ShortMessage.PROGRAM_CHANGE ) {
                            int channel = sm.getChannel();
                            currentProgramNumber = sm.getData1();
                            if ( !programNumberToNoteMessages.containsKey(currentProgramNumber) ) {
                                programNumberToNoteMessages.put( currentProgramNumber, new LinkedList<NoteMessage>() );
                            }
                            channelToProgramNumber.put( channel, currentProgramNumber );
                            System.out.println(channelToProgramNumber);
                        } else {
                            continue; // just ignore the message if it doesn't relate to the note or instrument
                        }
                    } else if ( message instanceof MetaMessage ){
                        MetaMessage mm = (MetaMessage) message;
                        int type = mm.getType();
                        if ( type == SET_TEMPO ) {
                            byte[] bytes = mm.getMessage();
                            int MPQNByte1 = bytes[3] & 0xFF;
                            int MPQNByte2 = bytes[4] & 0xFF;
                            int MPQNByte3 = bytes[5] & 0xFF;
                            
                            int microsecondsPerQuarterNote = MPQNByte1 * 0xFF * 0xFF + MPQNByte2 * 0xFF + MPQNByte3; 
                            millisecondsPerTick = microsecondsPerQuarterNote / (ticksPerBeat * beatsPerQuarterNote * 1000.0);
                            millisecondsPerTickAtTick.add( new Pair<Long,Double>(tick, millisecondsPerTick) );
                        } else if ( type == TIME_SIGNATURE ) {
                            // ticks per metronome click
                            byte[] bytes = mm.getMessage();
                            beatsPerQuarterNote *= 1;//(bytes[4] & 0xFF) / 4.0;
                            System.out.println("Time Signature: " + (bytes[3] & 0xFF) + "/" + (bytes[4] & 0xFF) + ", Met: " + (bytes[5] & 0xFF) );
                        }
                    }
        }
        
        // step 2. put in notes (but not simultaneous) into a sorted list (by tick time)
        Map<Integer,LinkedList<Triple<Long,Double,Note>>> programNumberToNotes = new HashMap<Integer,LinkedList<Triple<Long,Double,Note>>>();
        for ( int programNumber : programNumberToNoteMessages.keySet() ) {
            LinkedList<NoteMessage> noteMessages = programNumberToNoteMessages.get(programNumber);
            Collections.sort(noteMessages);
            
            LinkedList<Triple<Long,Double,Note>> notes = new LinkedList<Triple<Long,Double,Note>>();
            
            while ( !noteMessages.isEmpty() ) {
                NoteMessage noteMessage = noteMessages.remove();
                int value = noteMessage.value();
                millisecondsPerTick = noteMessage.millisecondsPerTick();
                int volume = noteMessage.volume();
                long tick = noteMessage.tick();
                int i = 0;
                NoteMessage nextNoteOffMessage = noteMessages.get(i);
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
                    int duration = (int) ( (offTick - tick) * millisecondsPerTick );
                    Note note = new Note( value, duration, volume);
                    note.setTick(tick);
                    notes.add( new Triple<Long,Double,Note>( tick, millisecondsPerTick, note ) );
                } else {
                    int duration = 0;
                    Note note = new Note( value, duration, volume);
                    note.setTick(tick);
                    notes.add( new Triple<Long,Double,Note>( tick, millisecondsPerTick, note ) );
                }
            }
            programNumberToNotes.put( programNumber, notes );
        }
        
        // step 3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        
        List<Voice> voices = new ArrayList<Voice>();
        Map<Integer,Long> firstTicks = new HashMap<Integer,Long>();
        
        int numberOfProgramsLeft = programNumberToNotes.keySet().size();
        int numberOfChannelsLeft = 15;
        int channelIdx = 0;
        int[] availableChannels = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15 };
        for ( int programNumber : programNumberToNotes.keySet() ) {
            int i = 0;
            long oldTick = 0; // arbitrary starting value with no meaning
            List<Integer> timesUntilNextElement = new ArrayList<Integer>();
            List<MusicElement> musicElements = new ArrayList<MusicElement>();
            LinkedList<Triple<Long,Double,Note>> notes = programNumberToNotes.get(programNumber);
            while ( !notes.isEmpty() ) {
                List<MusicElement> simultaneousNotes = new ArrayList<MusicElement>();
                Triple<Long,Double,Note> triple = notes.remove();
                long tick = triple.getLeft();
                millisecondsPerTick = triple.getMiddle();
                if ( i != 0 ) {
                    timesUntilNextElement.add( (int) ((tick - oldTick) * millisecondsPerTick) );
                } else {
                    firstTicks.put(programNumber,tick);
                }
                Note note = triple.getRight();
                simultaneousNotes.add(note);
                while ( !notes.isEmpty() ) {
                    Triple<Long,Double,Note> nextTriple = notes.peek();
                    if ( nextTriple.getLeft() == tick ) {
                        notes.remove();
                        Note simultaneousNote = nextTriple.getRight();
                        simultaneousNotes.add( simultaneousNote );
                    } else {
                        break;
                    }
                }
                if ( simultaneousNotes.size() == 1 ) {
                    musicElements.add(simultaneousNotes.get(0));
                } else if ( simultaneousNotes.size() > 1 ) {
                    musicElements.add( new Simultaneous(simultaneousNotes) );
                } else {
                    throw new RuntimeException("Something is wrong here...");
                }
                
                oldTick = tick;
                
                i++;
            }
            
            System.out.println("Program Number: " + programNumber + "Time until next: " + timesUntilNextElement);
            synth.loadInstrument(instruments[programNumber]);
            
            int numberOfChannelsPickedOff = (int) Math.ceil( ((double) numberOfProgramsLeft) / numberOfChannelsLeft );
            numberOfProgramsLeft -= numberOfChannelsPickedOff;
            numberOfChannelsLeft -= numberOfChannelsPickedOff;
            int[] channelsToUse = new int[numberOfChannelsPickedOff];
            for ( int j=0; j < numberOfChannelsPickedOff; j++ ) {
                channelsToUse[j] = availableChannels[channelIdx];
                channelIdx++;
            }
            voices.add( new MidiVoice( musicElements, timesUntilNextElement, new GeneralInstrument( programNumber, synth, channelsToUse ) ) );
        }
        
        // step 4. compute time until voices start
        Collections.sort(millisecondsPerTickAtTick, new Comparator<Pair<Long,Double>>() {
            @Override
            public int compare(Pair<Long, Double> pair, Pair<Long, Double> otherPair) {
                // TODO Auto-generated method stub
                return (int) (pair.getLeft() - otherPair.getLeft());
            }
        });
        Map<Integer,Integer> programNumberToTimeUntilVoiceStarts = new HashMap<Integer,Integer>();
        long lastTick = 0;
        for ( int programNumber : firstTicks.keySet() ) {
           long tick = firstTicks.get(programNumber);
           int timeUntilVoiceStarts = 0;
           for ( int i=0; i < millisecondsPerTickAtTick.size(); i++ ) {
               Pair<Long,Double> pair = millisecondsPerTickAtTick.get(i);
               long otherTick = pair.getLeft();
               millisecondsPerTick = pair.getRight();
               if ( tick >= otherTick ) {
                   lastTick = otherTick;
                   if ( i == 0 ) {
                       timeUntilVoiceStarts += (int) (otherTick * millisecondsPerTick);
                       System.out.println("Time until ...: " + timeUntilVoiceStarts);
                   } else {
                       timeUntilVoiceStarts += (int) ((otherTick - millisecondsPerTickAtTick.get(i-1).getLeft()) * millisecondsPerTick);
                       System.out.println("Time until ...: " + timeUntilVoiceStarts);
                   }
               } else {
                   break;
               }
           }
           timeUntilVoiceStarts += (int) ((tick - lastTick) * millisecondsPerTick);
           
           programNumberToTimeUntilVoiceStarts.put(programNumber,timeUntilVoiceStarts);
        }
        
        List<Integer> timesUntilVoiceStart = new ArrayList<Integer>();
        for ( Voice voice : voices ) {
            int programNumber = voice.instrument().getProgram();
            if ( programNumberToTimeUntilVoiceStarts.containsKey(programNumber) ) {
                timesUntilVoiceStart.add( programNumberToTimeUntilVoiceStarts.get( programNumber ) );
            } else {
                timesUntilVoiceStart.add( 0 );
            }
        }
        System.out.println(millisecondsPerTickAtTick);
        System.out.println(millisecondsPerTick);
        System.out.println("Times un voice start: " + timesUntilVoiceStart);
        System.out.println(firstTicks);
        
        return new Music( file.getName(), voices, timesUntilVoiceStart );
    }
    
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;

    public static void test1() throws Exception {
        Sequence sequence = MidiSystem.getSequence(new File("lostmymusic.mid"));
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
    
    public static void test2() {
        Sequencer sequencer = null;
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.setSequence(MidiSystem.getSequence(new File("lostmymusic.mid")));
            sequencer.open();
            sequencer.start();
            while(true) {
                if(sequencer.isRunning()) {
                    System.out.println(sequencer.getMicrosecondPosition());
                    try {
                        Thread.sleep(300); // Check every 0.3 second
                    } catch(InterruptedException ignore) {
                        break;
                    }
                } else {
                    break;
                }
            }
        
        } catch(Exception e) {
                System.out.println(e.toString());
        } finally {
            // Close resources
            sequencer.stop();
            sequencer.close();
        }
    }
    
    public static void main(String[] args) throws Exception {
        test1();
    }
}
