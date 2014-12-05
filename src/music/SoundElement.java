package music;

public interface SoundElement extends MusicElement {
    public int letter();
    
    public int duration();
    
    public SoundElement correspondingSoundElement( int letter );
}
