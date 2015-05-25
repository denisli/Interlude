package music;

public interface Voice {
    public Note next();
    
    public int timeUntilNextElement();
    
    public Handedness handedness();
    
    public Instrument instrument();
    
    public boolean ended();
    
    public int duration();
    
    public void restart();
}
