package music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Music {
    List<MusicElement> sequence = new ArrayList<MusicElement>();
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
}
