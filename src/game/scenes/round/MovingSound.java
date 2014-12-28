package game.scenes.round;

import game.Interlude;
import game.Renderable;
import game.settings.Orientation;
import music.Handedness;
import music.SoundElement;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import util.Pair;

public class MovingSound implements Renderable {
    private final static float SPEED = 1.0f/3500.0f; // how much of the screen in moves in 1 millisecond
    private float fractionX;
    private float fractionY;
    private int radius;
    private Color color = Color.black;
    private Handedness handedness;
    
    private final SoundElement soundElement;
    
    public MovingSound( float fractionX, float fractionY, SoundElement soundElement, Handedness handedness ) {
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.soundElement = soundElement;
        this.handedness = handedness;
    }
    
    public SoundElement soundElement() {
        return soundElement;
    }
    
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        Pair<Float,Float> actualPosition = Orientation.getPosition( fractionX, fractionY );
        float actualFractionX = actualPosition.getLeft();
        float actualFractionY = actualPosition.getRight();
        int centerX = (int) ( actualFractionX * containerWidth );
        int centerY = (int) ( actualFractionY * containerHeight );
        g.setColor( color );
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius ); 
    }
    
    public float fraction() {
        return fractionX;
    }
    
    public void update(int t) {
        fractionX += SPEED * t;
    }
    
    public static float speed() {
    	return SPEED;
    }
    
    public void init() {
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        radius = containerHeight / 50;
    }
    
    public boolean offScreen() {
        return fractionX > 1;
    }
    
    public Handedness handedness() {
        return handedness;
    }
    
    public void setFront() {
        color = Color.red;
    }
    
    public void setSecond() {
        color = Color.black;
    }
    
    public void setThird() {
        color = Color.black;
    }
}
