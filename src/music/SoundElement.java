package music;

public interface SoundElement extends MusicElement {
    public int integer();
    
    public SoundElement correspondingSoundElement( int letter );
}