package game.buttons;

import music.Note;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class NoteButton {
    private final int note;
    
    public NoteButton(int note) {
        this.note = note;
    }
    
    public void render(GameContainer gc, Graphics g) {
        switch (note) {
        case Note.A: return;
        case Note.B: return;
        case Note.C: return;
        case Note.D: return;
        case Note.E: return;
        case Note.F: return;
        case Note.G: return;
        }
        
    }
    
    public void update(GameContainer gc, int t) {
        return;
    }
}
