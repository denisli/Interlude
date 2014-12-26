package music;


public interface Voice {
    public SoundElement next();
    
    public int timeUntilNextElement();
    
    public Instrument instrument();
    
    public boolean ended();
    
    public int duration();
}
