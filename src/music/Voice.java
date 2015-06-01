package music;

public interface Voice {
    public Note nextNote();
    
    // This gives the time until the next element. At the beginning, this is the time until the first
    // note plays.
    public int timeUntilNextNote();
    
    public Instrument instrument();
    
    public boolean ended();
    
    // This is the time form when the music starts to when the last note plays.
    // FOR NOW...
    public int duration();
    
    public void restart();
}
