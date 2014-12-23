package music;

import game.InstrumentType;

public interface Voice {
    public MusicElement next();
    
    public int timeUntilNextElement();
    
    public Instrument instrument();
    
    public boolean ended();
}
