package game;

import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class MovingNote {
    private static int speed = 10;
    private Note note;
    private int centerX;
    private int centerY;
    private int radius;
    
    public MovingNote(Note note) {
        this.note = note;
    }
    
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        g.fillOval( centerX - radius, centerY - radius, radius, radius); 
    }
    
    public void update(GameContainer gc, int t) {
        centerX -= speed * t / 10;
    }
    
    public void init(GameContainer gc) {
        int containerWidth = gc.getWidth();
        int containerHeight = gc.getHeight();
        int increment = containerHeight / 10;
        centerX = containerWidth + 2 * radius;
        int letter = note.letter();
        if (letter == Note.G) {
            centerY = increment;
        } else if (letter == Note.F) {
            centerY = 2 * increment;
        } else if (letter == Note.E) {
            centerY = 3 * increment;
        } else if (letter == Note.D) {
            centerY = 4 * increment;
        } else if (letter == Note.C) {
            centerY = 5 * increment;
        } else if (letter == Note.B) {
            centerY = 6 * increment;
        } else if (letter == Note.A) {
            centerY = 7 * increment;
        } else {
            throw new IllegalArgumentException("MovingNote not given a valid note to represent");
        }
        radius = containerHeight / 26;
    }
    
    public Note note() {
        return note;
    }
    
    public boolean offScreen() {
        return centerX + radius < 0;
    }
}
