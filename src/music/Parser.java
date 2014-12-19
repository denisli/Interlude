package music;

import game.VoiceType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * TODO: finish this protocol
 * 
 * Grammar:
 * 
 * FILE := FIRST_LINE VOICE+
 * 
 * FIRST_LINE := TITLE NUM_VOICES NEWLINE
 * 
 * VOICE := TEMPO OCTAVE INSTRUMENT NEWLINE NOTES
 * 
 * NOTES := MUSIC_ELEMENT*
 * 
 * MUSIC_ELEMENT := NOTE | REST | SIMULTANEOUS
 * 
 * NOTE := LETTER DURATION OCTAVE* ACCIDENTAL
 * 
 * REST := . DURATION
 * 
 * SIMULTANEOUS := "[" NOTE* "]"
 * 
 */

public class Parser {
    private static final int VOLUME = 127;
    private static final char REST_MARKER = '.';
    private static final char SIMULTANEOUS_MARKER = '[';
    private static final String SIMULTANEOUS_END_MARKER = "]";
    
    public static Music fileToMusic(File file) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file) );
        List<Voice> voices = new ArrayList<Voice>();
        try {
            // first line of file contains the title
            String[] musicHeader = br.readLine().split(" ");
            String title = musicHeader[0];
            int numVoices = Integer.parseInt(musicHeader[1]);
            
            for (int i=0; i<numVoices; i++) {
                // first line
                String[] voiceHeader = br.readLine().split(" ");
                int tempo = Integer.parseInt(voiceHeader[0]);
                int octave = Integer.parseInt(voiceHeader[1]);
                Instrument instrument = Instrument.instrumentFromName(voiceHeader[2]);
                
                // second line
                Queue<String> notes = new LinkedList<String>(Arrays.asList(br.readLine().split(" ")));
                Voice voice = null;
                if ( numVoices == 1 ) {
                    voice = parseVoice(notes, octave, tempo, instrument, VoiceType.SINGLE);
                } else if ( numVoices == 2 && i == 0) {
                    voice = parseVoice(notes, octave, tempo, instrument, VoiceType.RIGHT);
                } else if ( numVoices == 2 && i == 1) {
                    voice = parseVoice(notes, octave, tempo, instrument, VoiceType.LEFT);
                }
                voices.add(voice);
            }
            return new Music(title, voices);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        throw new IllegalArgumentException("This code is not reachable"); // not reached
    }
    
    private static Voice parseVoice(Queue<String> notes, int octave, int tempo, Instrument instrument, VoiceType voiceType) {
        List<MusicElement> sequence = new ArrayList<MusicElement>(); 
        while ( !notes.isEmpty() ) {
            String element = notes.remove();
            char firstCharacter = element.charAt(0);
            if ( firstCharacter == REST_MARKER ) {
                Rest rest = parseRest( element, tempo );
                sequence.add(rest);
            } else if ( Character.isLetter(firstCharacter) ) {
                Note note = parseNote( element, octave, tempo );
                sequence.add(note);
            } else if ( firstCharacter == SIMULTANEOUS_MARKER ) {
                Simultaneous simultaneous = parseSimultaneous( notes, octave, tempo );
                sequence.add(simultaneous);
            } else {
                throw new IllegalArgumentException("First character: " + firstCharacter +
                                                   " is not valid!");
            }
        }
        return new TextVoice(sequence, instrument, voiceType);
    }
    
    private static Rest parseRest(String rest, int tempo) {
        boolean multiplyToDuration = true;
        float durationType = Note.QUARTER_NOTE;
        for ( int index=1; index<rest.length(); index++ ) {
            char character = rest.charAt(index);
            if (character == '/') {
                multiplyToDuration = false;
            } else if ( Character.isDigit( character ) ) {
                if (multiplyToDuration) {
                    durationType *= Character.getNumericValue( character );
                } else {
                    durationType /= Character.getNumericValue( character );
                }
            }
        }
        return new Rest( durationType, tempo );
    }
    
    private static Note parseNote(String note, int octave, int tempo) {
        boolean multiplyToDuration = true;
        int letter = 0;
        float durationType = Note.QUARTER_NOTE;
        int accidental = Note.NATURAL;
        for ( int index=0; index<note.length(); index++ ) {
            char character = note.charAt(index);
            if (character == 'A') {
                letter = Note.A;
            } else if (character == 'B') {
                letter = Note.B;
            } else if (character == 'C') {
                letter = Note.C;
            } else if (character == 'D') {
                letter = Note.D;
            } else if (character == 'E') {
                letter = Note.E;
            } else if (character == 'F') {
                letter = Note.F;
            } else if (character == 'G') {
                letter = Note.G;
            } else if ( Character.isDigit( character ) ) {
                if ( multiplyToDuration ) {
                    durationType *= Character.getNumericValue(character);
                } else {
                    durationType /= Character.getNumericValue(character);
                }
            } else if ( character == '/' ) {
                multiplyToDuration = false;
            } else if ( character == '_' ) {
                accidental = Note.decreaseAccidental( accidental );
            } else if ( character == '^' ) {
                accidental = Note.raiseAccidental( accidental );
            } else if ( character == '\'' ) {
                octave = Note.raiseOctave( octave );
            } else if ( character == ',' ) {
                octave = Note.decreaseOctave( octave );
            }
        }
        return new Note( letter, durationType, octave, accidental, VOLUME, tempo );
    }
    
    private static Simultaneous parseSimultaneous(Queue<String> notes, int octave, int tempo) {
        List<MusicElement> musicElements = new ArrayList<MusicElement>();
        while ( !notes.peek().equals(SIMULTANEOUS_END_MARKER) ) {
            String element = notes.remove();
            char firstCharacter = element.charAt(0);
            if ( firstCharacter == '.' ) {
                Rest rest = parseRest( element, tempo );
                musicElements.add(rest);
            } else if ( Character.isLetter( firstCharacter ) ) {
                Note note = parseNote( element, octave, tempo );
                musicElements.add(note);
            }
        }
        notes.remove(); // remove the end marker
        return new Simultaneous( musicElements );
    }
}
