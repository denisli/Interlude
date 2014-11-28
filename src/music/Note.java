package music;

/**
 * Note is an immutable class representing a musical note.
 * @author Denis
 *
 */
public class Note {
    public static final int C = 0;
    public static final int CS = 1;
    public static final int D = 2;
    public static final int DS = 3;
    public static final int E = 4;
    public static final int F = 5;
    public static final int FS = 6;
    public static final int G = 7;
    public static final int GS = 8;
    public static final int A = 9;
    public static final int AS = 10;
    public static final int B = 11;
    
    private final int note;
    private final int duration;
    private final int volume;
    private final int octave;
    //private final int tempo;
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
    
    public Note( int note, int duration, int volume, int octave ) {
        this.note = note;
        this.duration = duration;
        this.volume = volume;
        this.octave = octave;
        //this.tempo = tempo;
    }
    
    public int pitch() {
        return note + 12 * octave;
    }
    
    public int duration() {
        return duration;
    }
    
    public int volume() {
        return volume;
    }
    
    public int octave() {
        return octave;
    }
    
    public Note noteInOctave( int note ) {
        return new Note( note, duration, volume, octave );
    }
}
