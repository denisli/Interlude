package music;

public interface MusicElement {
    public static MusicElement note( int note, float durationType, int volume, int octave, int tempo, int accidental ) {
        return new Note( note, durationType, volume, octave, tempo, accidental );
    }
    
    public static MusicElement rest( int duration ) {
        return new Rest( duration );
    }
}
