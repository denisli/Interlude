package music;

public interface SoundElement extends MusicElement {
    public int letter();
    
    public SoundElement correspondingSoundElement( int letter );
}