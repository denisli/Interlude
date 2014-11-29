package music;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

public class Music {
    Queue<MusicElement> sequence = new LinkedList<MusicElement>();
    String title;
    int tempo;

    public void addElement(MusicElement element) {
        sequence.add(element);
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setTempo(int tempo) {
        this.tempo = tempo;
    }
    
    public MusicElement next() {
        return sequence.remove();
    }
    
    public boolean ended() {
        return sequence.isEmpty();
    }
}
