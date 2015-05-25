package music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Note is an immutable class representing a musical note.
 * @author Denis
 *
 */
public class Note {
    public static final int C = 0;
    //public static final int CS = 1;
    public static final int D = 2;
    //public static final int DS = 3;
    public static final int E = 4;
    public static final int F = 5;
    //public static final int FS = 6;
    public static final int G = 7;
    //public static final int GS = 8;
    public static final int A = 9;
    //public static final int AS = 10;
    public static final int B = 11;
    
    public static final List<Integer> NOTES_IN_ALPHABETICAL_ORDER = Collections.unmodifiableList(
            new ArrayList<Integer>( Arrays.asList(A, B, C, D, E, F, G))
            );
    
    
    public static final float WHOLE_NOTE = 4.0f;
    public static final float HALF_NOTE = 2.0f;
    public static final float QUARTER_NOTE = 1.0f;
    public static final float QUARTER_NOTE_TRIPLET = 2.0f/3;
    public static final float EIGHTH_NOTE = 1.0f/2;
    public static final float EIGHTH_NOTE_TRIPLET = 1.0f/3;
    public static final float SIXTEENTH_NOTE = 1.0f/4;
    public static final float SIXTEENTH_NOTE_TRIPLET = 1.0f/6;
    
    public static final int FLAT = -1;
    public static final int NATURAL = 0;
    public static final int SHARP = 1;
    
    private final int integer;
    private final int duration;
    private final int volume;
    private final int octave;
    private final int accidental;
    /**
     * Abstraction function: note is an integer corresponding to the letter of a musical note
     *                          (0: C, 1: C#, 2: D, 3: D#, 4: E, 5: F, 6: F#, 7: G, 8: G#, 9: A, 10: A#, 11: B) 
     *                       duration is how long the note lasts in milliseconds
     *                       volume is the the loudness of the note
     *                       octave is the octave that the note belongs in
     *                       tempo is the number of beats per minute in the music
     * Representation Invariant: pitch and volume must be between 0 and 127, inclusive
     *                           duration must be positive
     *                           octave must be an integer between 0 and 10, inclusive
     */
    
    public Note( int integer, float durationType, int octave, int accidental, int volume, int tempo) {
        this.integer = integer;
        this.octave = octave;
        this.accidental = accidental;
        this.volume = volume;
        
        final int standard = 60000; // give better name for this
        this.duration = (int) Math.floor( standard * durationType / tempo);
    }
    
    public Note( int integer, int octave, int accidental, int duration, int volume ) {
        this.integer = integer;
        this.octave = octave;
        this.accidental = accidental;
        this.duration = duration;
        this.volume = volume;
    }
    
    public Note( int value, int duration, int volume ) {
        int integer = value % 12;
        if ( !NOTES_IN_ALPHABETICAL_ORDER.contains(integer) ) {
            this.integer = integer - 1;
            this.accidental = Note.SHARP;
        } else {
            this.integer = integer;
            this.accidental = Note.NATURAL;
        }
        this.octave = value / 12;
        this.duration = duration;
        this.volume = volume;
    }
    
    private long tick;
    
    public void setTick(long tick) {
        this.tick = tick;
    }
    
    public long tick() {
        return tick;
    }
    
    public static int raiseAccidental(int accidental) {
        return accidental += 1;
    }
    
    public static int decreaseAccidental(int accidental) {
        return accidental -= 1;
    }
    
    public static int raiseOctave(int octave) {
        return octave += 1;
    }
    
    public static int decreaseOctave(int octave) {
        return octave -= 1;
    }
    
    public static String toLetter( int integer ) {
        if ( integer == Note.A ) {
            return "A";
        } else if ( integer == Note.B ) {
            return "B";
        } else if ( integer == Note.C ) {
            return "C";
        } else if ( integer == Note.D ) {
            return "D";
        } else if ( integer == Note.E ) {
            return "E";
        } else if ( integer == Note.F ) {
            return "F";
        } else if ( integer == Note.G ) {
            return "G";
        } else {
            throw new IllegalArgumentException("A valid note was not given");
        }
    }
    
    public static int toInteger( String string ) {
        if ( string.equals("A") ) {
            return Note.A;
        } else if ( string.equals("B") ) {
            return Note.B;
        } else if ( string.equals("C") ) {
            return Note.C;
        } else if ( string.equals("D") ) {
            return Note.D;
        } else if ( string.equals("E") ) {
            return Note.E;
        } else if ( string.equals("F") ) {
            return Note.F;
        } else if ( string.equals("G") ) {
            return Note.G;
        } else {
            throw new IllegalArgumentException("String does not represent a valid note letter");
        }
    }
    
    public int integer() {
        return integer;
    }
    
    public int octave() {
        return octave;
    }
    
    public int pitch() {
        return Math.max(0,Math.min(127,integer + 12 * octave + accidental)); // bound the answer between 0 and 127
    }
    
    public int volume() {
        return volume;
    }
    
    /**
     * Returns time in milliseconds of the note
     * @return
     */
    public int duration() {
        return duration;
    }
    
    /**
     * Returns the time (in milliseconds) until the next element
     * @return time until next note
     */
    public void bePlayed(Instrument instrument) {
        instrument.play( this );
    }
    
    @Override
    public boolean equals(Object other) {
        if (! (other instanceof Note) ) {
            return false;
        } else {
            Note otherNote = (Note) other;
            return this.integer == otherNote.integer && this.duration == otherNote.duration &&
                   this.volume == otherNote.volume && this.octave == otherNote.octave &&
                   this.accidental == otherNote.accidental;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Note: %d %d %d %d %d",integer,octave,accidental,duration,volume);
    }
    
    @Override
    public int hashCode() {
        return integer + volume + duration;
    }
}
