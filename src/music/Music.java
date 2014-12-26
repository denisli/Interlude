package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Music {
    private final String title;
    private final List<Voice> voices;
    private final List<Integer> timesUntilVoiceStarts;
    
    
    public Music(String title, List<Voice> voices, List<Integer> timesUntilVoiceStarts) {
        this.title = title;
        this.voices = voices; // voices sorted by start time
        this.timesUntilVoiceStarts = timesUntilVoiceStarts;
    }
    
    public boolean isMultiVoice() {
        return voices.size() > 1;
    }
    
    public List<Integer> timesUntilVoiceStarts() {
        return this.timesUntilVoiceStarts;
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
}
