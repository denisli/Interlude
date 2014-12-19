package music.parser;

import game.VoiceType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import util.Pair;
import music.GeneralInstrument;
import music.MidiVoice;
import music.Music;
import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.Voice;

public class MidiParser {
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    
    public static Music parse(File file, int splittingOctave) {
        // There are 3 steps to this:
        //      1. put in the note messages into a sorted list (by tick time)
        //      2. put in notes (but not simultaneous) into a sorted list (by tick time)
        //      3. put in sound elements into the a sorted list (by tick time)
        //         and also put in time between elements into a list (sorted by the order in which they occur)
        
        // step 1.
        Map<Integer,LinkedList<NoteMessage>> programNumberToNoteMessages = new HashMap<Integer,LinkedList<NoteMessage>>();
        int currentProgramNumber = 1000; // dummy number for initial program number
        
        try {
            Sequence sequence = MidiSystem.getSequence( file );
            for (Track track : sequence.getTracks()) {
                for ( int i=0; i < track.size(); i++ ) {
                    MidiEvent event = track.get(i);
                    long tick = event.getTick();
                    MidiMessage message = event.getMessage();
                    if ( message instanceof ShortMessage ) {
                        ShortMessage sm = (ShortMessage) message;
                        if ( sm.getCommand() == ShortMessage.NOTE_ON ) {
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( currentProgramNumber );
                            if ( sm.getData2() != 0 ) { 
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), true ) );
                            } else {
                                noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), false ) );
                            }
                        } else if ( sm.getCommand() == ShortMessage.NOTE_OFF ) {
                            List<NoteMessage> noteMessages = programNumberToNoteMessages.get( currentProgramNumber );
                            noteMessages.add( new NoteMessage( tick, sm.getData1(), sm.getData2(), false ) );
                        } else if ( sm.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
                            currentProgramNumber = sm.getData1();
                            if ( !programNumberToNoteMessages.containsKey(currentProgramNumber) ) {
                                programNumberToNoteMessages.put( currentProgramNumber, new LinkedList<NoteMessage>() );
                            }
                        } else {
                            continue; // just ignore the message if it doesn't relate to the note or instrument
                        }
                    }
                }
            }
        } catch ( IOException | InvalidMidiDataException e ) {
            e.printStackTrace();
        }
        
        // step 2.
        Map<Integer,LinkedList<Pair<Long,Note>>> programNumberToNotes = new HashMap<Integer,LinkedList<Pair<Long,Note>>>();
        for ( int programNumber : programNumberToNoteMessages.keySet() ) {
            LinkedList<NoteMessage> noteMessages = programNumberToNoteMessages.get(programNumber);
            Collections.sort(noteMessages);
            
            LinkedList<Pair<Long,Note>> notes = new LinkedList<Pair<Long,Note>>();
            
            while ( !noteMessages.isEmpty() ) {
                NoteMessage noteMessage = noteMessages.remove();
                int value = noteMessage.value();
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
                notes.add( new Pair<Long,Note>( tick, new Note( value, (int) (offTick - tick), volume) ) );
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
            LinkedList<Pair<Long,Note>> notes = programNumberToNotes.get(programNumber);
            while ( !notes.isEmpty() ) {
                List<MusicElement> simultaneousNotes = new ArrayList<MusicElement>();
                Pair<Long,Note> pair = notes.remove();
                long tick = pair.getLeft();
                if ( i != 0 ) {
                    timesUntilNextElement.add( (int) (tick - oldTick) );
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
    
    public static void main(String[] args) throws InvalidMidiDataException, IOException {
        Sequence sequence = MidiSystem.getSequence(new File("suzumiya-haruhi-no-yuuutsu-bouken-desho-desho.mid"));

        int trackNumber = 0;
        Track[] tracks = sequence.getTracks();
        //System.out.println("Number of tracks: " + tracks.length);
        for (Track track :  tracks) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else {
                        System.out.println("Command:" + sm.getCommand());
                    }
                } else {
                    MetaMessage mm = (MetaMessage) message;
                    System.out.println("Type: " + mm.getType() + ", Data: " + mm.getData().length );
                }
            }

            System.out.println();
        }
    }
}
