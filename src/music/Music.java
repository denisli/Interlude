package music;

import java.util.List;

public class Music {
    private final String title;
    private final List<Voice> voices;
    
    
    public Music(String title, List<Voice> voices ) {
        this.title = title;
        this.voices = voices;
    }
    
    public boolean isMultiInstrument() {
        return voices.size() > 1;
    }
    
    public String title() {
        return title;
    }
    
    public List<Voice> voices() {
    	return voices;
    }
    
    public int duration() {
        int longestDuration = 0;
        for ( Voice voice : voices ) {
            longestDuration = Math.max( longestDuration, voice.duration() );
        }
        return longestDuration;
    }
    
    public void restart() {
        voices.stream().forEach( instrumentPiece -> instrumentPiece.restart() );
    }
}
