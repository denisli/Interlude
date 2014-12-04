package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

public class Music {
    private final Queue<MusicElement> sequence;
    private final String title;
    private final int tempo;
    private final Instrument instrument;
    
    public Music(String title, int tempo, Instrument instrument, Queue<MusicElement> sequence) {
        this.title = title;
        this.tempo = tempo;
        this.instrument = instrument;
        this.sequence = sequence;
    }
    
    public static Music godKnows() {
        try {
            return Parser.fileToMusic(new File("res/music.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("stuff");
        }
    }
    
    public void add(MusicElement note) {
        sequence.add(note);
    }
    
    public String getTitle() {
        return title;
    }
    
    public int tempo() {
        return tempo;
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public MusicElement next() {
        return sequence.remove();
    }
    
    public boolean ended() {
        return sequence.isEmpty();
    }
}
