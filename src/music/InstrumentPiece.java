package music;

import java.util.List;

public class InstrumentPiece {
    private final List<Voice> voices;
    private final List<Integer> timesUntilVoiceStarts;
    private final Instrument instrument;
    
    public InstrumentPiece(List<Voice> voices, List<Integer> timesUntilVoiceStarts, Instrument instrument) {
        this.voices = voices;
        this.timesUntilVoiceStarts = timesUntilVoiceStarts;
        this.instrument = instrument;
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public List<Voice> voices() {
        return voices;
    }
    
    public int duration() {
        int maxDuration = 0;
        for ( int i = 0; i < voices.size(); i++ ) {
            Voice voice = voices.get(i);
            int timeVoiceStarts = timesUntilVoiceStarts.get(i);
            maxDuration = Math.max( maxDuration, voice.duration() + timeVoiceStarts );
        }
        return maxDuration;
    }
    
    public List<Integer> timesUntilVoiceStarts() {
        return timesUntilVoiceStarts;
    }
}
