package music;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiChannel;

/**
 * Note is an immutable class representing a musical note.
 * @author Denis
 *
 */
public class Note implements SoundElement {
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
    
    public static final float WHOLE_NOTE = 1;
    public static final float HALF_NOTE = 1.0f/2;
    public static final float QUARTER_NOTE = 1.0f/4;
    public static final float QUARTER_NOTE_TRIPLET = 1.0f/3;
    public static final float EIGHTH_NOTE = 1.0f/8;
    public static final float EIGHTH_NOTE_TRIPLET = 1.0f/6;
    public static final float SIXTEENTH_NOTE = 1.0f/16;
    public static final float SIXTEENTH_NOTE_TRIPLET = 1.0f/12;
    
    public static final int FLAT = -1;
    public static final int NATURAL = 0;
    public static final int SHARP = 1;
    
    private final int letter;
    private final float durationType;
    private final int volume;
    private final int octave;
    private final int accidental;
    private final int tempo;
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
    
    public Note( int letter, float durationType, int octave, int accidental, int volume, int tempo) {
        this.letter = letter;
        this.durationType = durationType;
        this.octave = octave;
        this.accidental = accidental;
        this.volume = volume;
        this.tempo = tempo;
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
    
    public int letter() {
        return letter;
    }
    
    public int pitch() {
        return Math.max(0,Math.min(127,letter + 12 * octave + accidental)); // bound the answer between 0 and 127
    }
    
    public int volume() {
        return volume;
    }
    
    /**
     * Returns time in milliseconds of the note
     * @return
     */
    @Override
    public int duration() {
        final int standard = 240000; // need a better name for this...?
        return (int) Math.floor( standard * durationType / tempo );
    }
    
    /**
     * Returns the time (in milliseconds) until the next element
     * @return time until next note
     */
    @Override
    public int timeUntilNextElement() {
        return duration();
    }
    
    @Override
    public void bePlayed(Instrument instrument) {
        instrument.play( this );
    }
    
    @Override
    public Note correspondingSoundElement( int letter ) {
        if (letter == Simultaneous.S) {
            return new Note( 3, durationType, octave, accidental, volume, tempo ); // TODO: FIX THIS TO SOMETHING MORE LEGIT
        }
        return new Note( letter, durationType, octave, accidental, volume, tempo );
    }
    
    @Override
    public boolean isRest() {
        return false;
    }
    
    @Override
    public boolean equals(Object other) {
        if (! (other instanceof Note) ) {
            return false;
        } else {
            Note otherNote = (Note) other;
            return this.letter == otherNote.letter && this.durationType == otherNote.durationType &&
                   this.volume == otherNote.volume && this.octave == otherNote.octave &&
                   this.tempo == otherNote.tempo && this.accidental == otherNote.accidental;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Note: %d %.3f %d %d %d %d",letter,durationType,volume,octave,tempo,accidental);
    }
    
    @Override
    public int hashCode() {
        return letter + volume + duration();
    }
}
