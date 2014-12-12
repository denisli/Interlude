package game.buttons;

import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

public abstract class NoteButton implements Button {
    private final int note;
    
    public NoteButton( int note ) {
        this.note = note;
    }
    
    public int note() {
        return note;
    }

    @Override
    public boolean isClicked(Input input) {
        // TODO Auto-generated method stub
        return false;
    }
}
