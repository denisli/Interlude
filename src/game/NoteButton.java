package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class NoteButton {
    private final Note note;
    
    public NoteButton(Note note) {
        this.note = note;
    }
    
    public void render(GameContainer gc, Graphics g) {
        switch (note) {
        case A: return;
        case B: return;
        case C: return;
        case D: return;
        case E: return;
        case F: return;
        case G: return;
        }
        
    }
    
    public void update(GameContainer gc, int t) {
        return;
    }
}
