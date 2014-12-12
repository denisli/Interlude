package game;

import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.SoundElement;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class OneVoiceMovingSound extends MovingSound {
    private final static int SPEED = 10;
    private int centerX;
    private int centerY;
    private int radius;
    
    public OneVoiceMovingSound(SoundElement soundElement) {
        super( soundElement );
    }
    
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius ); 
    }
    
    public void update(GameContainer gc, int t) {
        centerX -= SPEED * t / 10;
    }
    
    public void init(GameContainer gc) {
        int containerWidth = gc.getWidth();
        int containerHeight = gc.getHeight();
        int increment = containerHeight / 10;
        centerX = containerWidth + 2 * radius;
        int letter = soundElement().letter();
        if (letter == Note.A) {
            centerY = increment;
        } else if (letter == Note.B) {
            centerY = 2 * increment;
        } else if (letter == Note.C) {
            centerY = 3 * increment;
        } else if (letter == Note.D) {
            centerY = 4 * increment;
        } else if (letter == Note.E) {
            centerY = 5 * increment;
        } else if (letter == Note.F) {
            centerY = 6 * increment;
        } else if (letter == Note.G) {
            centerY = 7 * increment;
        } else if (letter == Simultaneous.S ){
            centerY = 8 * increment;
        } else {
            throw new IllegalArgumentException("MovingNote not given a valid note to represent");
        }
        radius = containerHeight / 45;
    }
    
    public boolean offScreen(GameContainer gc) {
        return centerX + radius < 0;
    }
}
