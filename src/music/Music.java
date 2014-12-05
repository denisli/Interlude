package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;

public class Music {
    private final Queue<MusicElement> sequence;
    private final Queue<Float> restTimes; // ith element is the time until the next SoundElement is played
    private final String title;
    private final int tempo;
    private final Instrument instrument;
    
    public Music(String title, int tempo, Instrument instrument, Queue<MusicElement> sequence, Queue<Float> restTimes) {
        this.title = title;
        this.tempo = tempo;
        this.instrument = instrument;
        this.sequence = sequence;
        this.restTimes = restTimes;
    }
    
    public static Music godKnows() {
        try {
            return Parser.fileToMusic(new File("res/music.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("stuff");
        }
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
    
    // time in milliseconds
    public int timeUntilNextSound() {
        final int standard = 60000; // need a better name for this...?
        return (int) Math.floor( standard * restTimes.remove() / tempo );
    }
    
    // I must use timeUntilNextSound after calling next. Maybe there is a better way to design this so that I
    // don't force this requirement on myself. But for now, this doesn't seem to bad.
}
