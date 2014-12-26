package music;


public interface Voice {
    public SoundElement next();
    
    public int timeUntilNextElement();
    
    public Handedness handedness();
    
    public Instrument instrument();
    
    public boolean ended();
    
    public int duration();
}
