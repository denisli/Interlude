package game.buttons;

import music.Note;
import music.Simultaneous;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public class NoteButton implements Button {
    private final int note;
    private int centerX;
    private int centerY;
    private int radius;
    
    public NoteButton(int note) {
        this.note = note;
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval( centerX - radius, centerY - radius, radius, radius); 
    }
    
    @Override
    public void update(GameContainer gc, int t) {
        return;
    }
    
    @Override
    public void init(GameContainer gc) {
        int containerWidth = gc.getWidth();
        int containerHeight = gc.getHeight();
        int increment = containerHeight / 10;
        centerX = containerWidth / 10;
        if (note == Note.A) {
            centerY = increment;
        } else if (note == Note.B) {
            centerY = 2 * increment;
        } else if (note == Note.C) {
            centerY = 3 * increment;
        } else if (note == Note.D) {
            centerY = 4 * increment;
        } else if (note == Note.E) {
            centerY = 5 * increment;
        } else if (note == Note.F) {
            centerY = 6 * increment;
        } else if (note == Note.G) {
            centerY = 7 * increment;
        } else if (note == Simultaneous.S) {
            centerY = 8 * increment;
        } else {
            throw new IllegalArgumentException("Note button not given a valid note to represent");
        }
        radius = containerHeight / 25;
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return 2 * radius;
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return 2 * radius;
    }

    @Override
    public boolean isClicked(Input input) {
        // TODO Auto-generated method stub
        return false;
    }
}
