package game;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import music.Note;
import music.Simultaneous;
import music.SoundElement;

public class TwoVoiceMovingSound extends MovingSound{
    private static final int NUM_LETTERS = 8; // number of notes represented by letters
    
    private final Hand hand;
    private int centerX;
    private int centerY;
    private int radius;
    
    public TwoVoiceMovingSound( SoundElement soundElement, Hand hand ) {
        super( soundElement );
        this.hand = hand;
    }
    
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius); 
    }
    
    public void update(GameContainer gc, int t) {
        centerY += MovingSound.SPEED * t / 10;
    }
    
    public void init(GameContainer gc) {
        int containerWidth = gc.getWidth();
        int containerHeight = gc.getHeight();
        int increment = containerWidth / 20;
        centerY = -2 * radius;
        int letter = soundElement().letter();
        if (letter == Note.A) {
            centerX = 2 * increment;
        } else if (letter == Note.B) {
            centerX = 3 * increment;
        } else if (letter == Note.C) {
            centerX = 4 * increment;
        } else if (letter == Note.D) {
            centerX = 5 * increment;
        } else if (letter == Note.E) {
            centerX = 6 * increment;
        } else if (letter == Note.F) {
            centerX = 7 * increment;
        } else if (letter == Note.G) {
            centerX = 8 * increment;
        } else if (letter == Simultaneous.S ){
            centerX = 9 * increment;
        } else {
            throw new IllegalArgumentException("MovingNote not given a valid note to represent");
        }
        if ( hand == Hand.RIGHT ) {
            centerX += NUM_LETTERS * increment;
        }
        
        radius = containerHeight / 45;
    }
    
    public boolean offScreen(GameContainer gc) {
        return centerY + radius > gc.getHeight();
    }
}
