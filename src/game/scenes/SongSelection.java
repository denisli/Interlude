package game.scenes;

import java.util.LinkedList;
import java.util.Queue;

import music.Instrument;

public class SongSelection implements Scene {
    private String songTitle;
    private Instrument instrument;
    
    private Queue<Integer> notesOnScreen = new LinkedList<Integer>();
    
    public SongSelection(String songTitle, Instrument instrument) {
        this.songTitle = songTitle;
        this.instrument = instrument;
    }
}
