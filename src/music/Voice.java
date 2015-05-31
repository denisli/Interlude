package music;

public interface Voice {
    public Note next();
    
    // when a voice starts within a music
    public int startTime();
    
    public int timeUntilNextElement();
    
    public Instrument instrument();
    
    public boolean ended();
    
    // This is the time between the end time and start time. The start time is 
    // the time specified in the startTime() method. The end
    // time is when the voice's last note plays.
    public int duration();
    
    public void restart();
}
