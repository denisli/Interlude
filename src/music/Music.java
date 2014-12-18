package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Music {
    private final String title;
    private final List<Voice> voices;
    
    
    public Music(String title, List<Voice> voices) {
        this.title = title;
        this.voices = voices;
    }
    
    public static Music godKnows() {
        try {
            return Parser.fileToMusic(new File("res/new_music2.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("I should be able to find the file...");
        }
    }
    
    public boolean isMultiVoice() {
        return voices.size() > 1;
    }
    
    public String title() {
        return title;
    }
    
    public List<Voice> voices() {
        return voices;
    }
}
