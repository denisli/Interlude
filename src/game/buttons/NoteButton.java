package game.buttons;

import music.Note;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class NoteButton implements Button {
    private final Rectangle boundingBox;
    private final static int X = 100;
    private final static int WIDTH = 500;
    private final static int HEIGHT = 100;
    private final int note;
    
    public NoteButton(int note) {
        this.note = note;
        if (note == Note.G) {
            boundingBox = new Rectangle(X, 100, WIDTH, HEIGHT);
        } else if (note == Note.F) {
            boundingBox = new Rectangle(X, 300, WIDTH, HEIGHT);
        } else if (note == Note.E) {
            boundingBox = new Rectangle(X, 500, WIDTH, HEIGHT);
        } else if (note == Note.D) {
            boundingBox = new Rectangle(X, 700, WIDTH, HEIGHT);
        } else if (note == Note.C) {
            boundingBox = new Rectangle(X, 900, WIDTH, HEIGHT);
        } else if (note == Note.B) {
            boundingBox = new Rectangle(X, 1100, WIDTH, HEIGHT);
        } else if (note == Note.A) {
            boundingBox = new Rectangle(X, 1300, WIDTH, HEIGHT);
        } else {
            throw new IllegalArgumentException("Not a valid note");
        }
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        g.fillRect(boundingBox.getX(), boundingBox.getY(), WIDTH, HEIGHT);
        
    }
    
    @Override
    public void update(GameContainer gc, int t) {
        return;
    }
    
    @Override
    public void init(GameContainer gc) {
        return;
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return 0;
    }
}
