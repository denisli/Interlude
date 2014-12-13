package game.note_marker;

import game.Hand;
import game.buttons.Button;
import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

public abstract class NoteMarker implements Button {
    private final int note;
    private Runnable effect = (Runnable) () -> {};
    
    public NoteMarker( int note ) {
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
    
    @Override
    public void setEffect(Runnable effect) {
        this.effect = effect;
    }
    
    @Override
    public void callEffect() {
        effect.run();
    }
    
    public static NoteMarker oneVoiceNoteMarker( int note ) {
        return new OneVoiceNoteMarker( note );
    }
    
    public static NoteMarker twoVoiceNoteMarker( int note, Hand hand ) {
        return new TwoVoiceNoteMarker( note, hand );
    }
}
