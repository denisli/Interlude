package music;

public interface SoundElement {
    public int integer();
    
    public int duration();
    
    public void bePlayed( Instrument instrument );
    
    public SoundElement correspondingSoundElement( int letter );
}