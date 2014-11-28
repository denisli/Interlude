package game;

import music.Note;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class MovingNote {
    private static double speed;
    private Note note;
    private double x;
    private double y;
    
    public Note note() {
        return note;
    }
    
    public void render(GameContainer gc, Graphics g) {
        
    }
    
    public void update(GameContainer gc, int t) {
        x -= speed * t;
    }
}
