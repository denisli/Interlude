package music;

public interface MusicElement {
    public static MusicElement note( int note, int duration, int volume, int octave ) {
        return new Note( note, duration, volume, octave );
    }
    
    public static MusicElement rest( int duration ) {
        return new Rest( duration );
    }
}
