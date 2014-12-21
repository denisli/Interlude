package music.parser;

import game.VoiceType;

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
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import util.Triple;
import music.GeneralInstrument;
import music.MidiVoice;
import music.Music;
import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.Voice;

public class MidiParser {
    private static final int SET_TEMPO = 0x51;
    private static final int TIME_SIGNATURE = 0x58;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
    public static Music parse(File file, int splittingOctave) {
        // There are 3 steps to this:
        //      1. put in the note messages into a sorted list (by tick time)
        //      2. put in notes (but not simultaneous) into a sorted list (by tick time)
        //      3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        
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
                    if ( message instanceof MetaMessage ) { // the only meta mesage we care about now is 81.
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        });
        int ticksPerQuarterNote = 0;
        try {
            Sequence sequence = MidiSystem.getSequence( file );
            ticksPerQuarterNote = sequence.getResolution();
            
            for (Track track : sequence.getTracks()) {
                for ( int i=0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    midiEvents.add(event);
                }
            }
        } catch ( IOException | InvalidMidiDataException e ) {
            e.printStackTrace();
        }
        
        // step 1.
        Map<Integer,LinkedList<NoteMessage>> programNumberToNoteMessages = new HashMap<Integer,LinkedList<NoteMessage>>();
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
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( currentProgramNumber );
                            if ( sm.getData2() != 0 ) { 
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), true ) );
                            } else {
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), false ) );
                            }
                        } else if ( command == ShortMessage.NOTE_OFF ) {
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( currentProgramNumber );
                            noteMessages.add( new NoteMessage( tick, sm.getData1(), millisecondsPerTick, sm.getData2(), false ) );
                        } else if ( command == ShortMessage.PROGRAM_CHANGE ) {
                            currentProgramNumber = sm.getData1();
                            if ( !programNumberToNoteMessages.containsKey(currentProgramNumber) ) {
                                programNumberToNoteMessages.put( currentProgramNumber, new LinkedList<NoteMessage>() );
                            }
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
                            millisecondsPerTick = microsecondsPerQuarterNote / (ticksPerQuarterNote * 1000.0);
                        } else if ( type == TIME_SIGNATURE ) {
                            // ticks per metronome click
                        }
                    }
        }
        
        // step 2.
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
                    nextNoteOffMessage = noteMessages.get(i);
                }
                NoteMessage noteOffMessage = noteMessages.remove(i);
                long offTick = noteOffMessage.tick();
                int duration = (int) ( (offTick - tick) * millisecondsPerTick );
                notes.add( new Triple<Long,Double,Note>( tick, millisecondsPerTick, new Note( value, duration, volume) ) );
            }
            programNumberToNotes.put( programNumber, notes );
        }
        
        // step 3.
        List<Voice> voices = new ArrayList<Voice>();
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
            voices.add( new MidiVoice( musicElements, timesUntilNextElement, new GeneralInstrument( programNumber ), VoiceType.SINGLE ) );
        }
        
        return new Music( file.getName(), voices );
    }
}
