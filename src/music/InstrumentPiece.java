package music;

import java.util.List;

public class InstrumentPiece {
    private final List<Voice> voices;
    private final List<Integer> timesUntilVoicesStart;
    private final Instrument instrument;
    
    public InstrumentPiece(List<Voice> voices, List<Integer> timesUntilVoicesStart, Instrument instrument) {
        this.voices = voices;
        this.timesUntilVoicesStart = timesUntilVoicesStart;
        this.instrument = instrument;
    }
    
    public List<Voice> voices() {
        return voices;
    }
    
    public List<Integer> timesUntilVoicesStart() {
        return this.timesUntilVoicesStart;
    }
    
    public int duration() {
        int maxDuration = 0;
        for ( int i = 0; i < voices.size(); i++ ) {
            Voice voice = voices.get(i);
            int timeVoiceStarts = timesUntilVoicesStart.get(i);
            maxDuration = Math.max( maxDuration, voice.duration() + timeVoiceStarts );
        }
        return maxDuration;
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public void restart() {
        voices.stream().forEach( voice -> voice.restart() );
    }
}
