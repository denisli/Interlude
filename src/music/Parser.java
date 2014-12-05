package music;

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
 * TODO: make this protocol more clear
 * Only two lines in file? Spec: the file is in the correct format! Else, do whatever (for now)
 * 
 * Grammar:
 * 
 * FIRST LINE := TITLE TEMPO INSTRUMENTNAME
 * 
 * INSTRUMENTNAME must be all lowercase
 * 
 * OTHER_LINE := NOTE | REST
 * 
 * SIMULTANEOUS := SIMULTANEOUS_MARKER NOTE w/o marker ... NOTE w/o marker END
 * 
 * NOTE := NOTE_MARKER LETTER DURATION_TYPE_NAME VOLUME, OCTAVE, ACCIDENTAL
 * 
 * REST := REST_MARKER DURATION_TYPE_NAME
 * 
 * LETTER := "A" | "B" | ... | "G"
 * 
 * DURATION_TYPE_NAME := "W" - whole note, "H" - half note, "Q" - quarter note, "QT" - quarter triplet,
 * "E" - eighth note, "ET" - eighth triplet, "S" - sixteenth, "ST" - sixteenth triplet
 * 
 * VOLUME := some integer between 0 and 127 inclusive
 * 
 * ACCIDENTAL := "flat", "sharp", "natural" (flat, sharp, natural)
 */

public class Parser {
    private static final String NOTE_MARKER = "note";
    private static final String REST_MARKER = "rest";
    private static final String SIMULTANEOUS_MARKER = "simultaneous";
    private static final String END_MARKER = "end";
    
    public static Music fileToMusic(File file) throws FileNotFoundException {
        Queue<MusicElement> sequence = new LinkedList<MusicElement>(); 
        
        BufferedReader br = new BufferedReader(new FileReader(file) );
        try {
            // first line of file contains the title, tempo, and instrument of music (space-separated)
            String[] titleTempoInstrumentName = br.readLine().split(" ");
            String title = titleTempoInstrumentName[0];
            int tempo = Integer.valueOf(titleTempoInstrumentName[1]);
            Instrument instrument = Instrument.instrumentFromName(titleTempoInstrumentName[2]);
            
            Queue<String> notes = new LinkedList<String>(Arrays.asList(br.readLine().split(" ")));
            
            while (!notes.isEmpty()) {
                String marker = notes.remove();
                if (marker.equals(NOTE_MARKER)) {
//                    int letter = Note.letter(notes.remove());
//                    float durationType = Note.durationTypeFromName(notes.remove());
//                    //int volume = Integer.valueOf(notes.remove());
//                    int octave = Integer.valueOf(notes.remove());
//                    int accidental = Note.accidentalFromName(notes.remove());
//                    Note note = new Note(letter, durationType, 127, octave, tempo, accidental);
                    Note note = processNote(notes, tempo);
                    sequence.add(note);
                } else if (marker.equals(REST_MARKER)) {
                    float durationType = Note.durationTypeFromName(notes.remove());
                    sequence.add(new Rest(durationType, tempo));
                } else if (marker.equals(SIMULTANEOUS_MARKER)){
                    List<Note> simultaneousNotes = new ArrayList<Note>();
                    while ( !notes.peek().equals(END_MARKER) ) {
                        Note note = processNote(notes, tempo);
                        simultaneousNotes.add(note);
                    }
                    notes.remove(); // remove the end marker
                    Simultaneous simultaneous = new Simultaneous(simultaneousNotes);
                    sequence.add(simultaneous);
                } else {
                    throw new IllegalArgumentException("There needs to be a marker to indicate the music element type");
                }
            }
            
            Music music = new Music(title, tempo, instrument, sequence);
            return music;
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
    
    /**
     * This method modifies the given queue! Be careful of how this is used.
     * @param notes
     * @return
     */
    private static Note processNote(Queue<String> notes, int tempo) {
        int letter = Note.letter(notes.remove());
        float durationType = Note.durationTypeFromName(notes.remove());
        //int volume = Integer.valueOf(notes.remove());
        int octave = Integer.valueOf(notes.remove());
        int accidental = Note.accidentalFromName(notes.remove());
        Note note = new Note(letter, durationType, 127, octave, tempo, accidental);
        return note;
    }
}
