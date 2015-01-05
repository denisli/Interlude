package music;

import java.util.ArrayList;
import java.util.List;

public class Music {
    private final String title;
    private final List<InstrumentPiece> instrumentPieces;
    
    
    public Music(String title, List<InstrumentPiece> instrumentPieces ) {
        this.title = title;
        this.instrumentPieces = instrumentPieces;
    }
    
    public boolean isMultiInstrument() {
        return instrumentPieces.size() > 1;
    }
    
    public String title() {
        return title;
    }
    
    public List<Voice> voices() {
        List<Voice> voices = new ArrayList<Voice>();
        for ( InstrumentPiece instrumentPiece : instrumentPieces ) {
            for ( Voice voice : instrumentPiece.voices() ) {
                voices.add(voice);
            }
        }
        return voices;
    }
    
    public List<Integer> timesUntilVoicesStart() {
        List<Integer> timesUntilVoicesStart = new ArrayList<Integer>();
        for (InstrumentPiece instrumentPiece : instrumentPieces) {
            for ( int timeUntilVoiceStarts : instrumentPiece.timesUntilVoicesStart() ) {
                timesUntilVoicesStart.add(timeUntilVoiceStarts);
            }
        }
        return timesUntilVoicesStart;
    }
    
    public List<InstrumentPiece> instrumentPieces() {
        return instrumentPieces;
    }
    
    public int duration() {
        int longestDuration = 0;
        for ( InstrumentPiece instrumentPiece : instrumentPieces ) {
            longestDuration = Math.max( longestDuration, instrumentPiece.duration() );
        }
        return longestDuration;
    }
    
    public void restart() {
        instrumentPieces.stream().forEach( instrumentPiece -> instrumentPiece.restart() );
    }
}
