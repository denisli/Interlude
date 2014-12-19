package music;

import game.VoiceType;

public interface Voice {
    public MusicElement next();
    
    public int timeUntilNextElement();
    
    public Instrument instrument();
    
    public boolean ended();
    
    public VoiceType voiceType();
}
