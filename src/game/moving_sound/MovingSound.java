package game.moving_sound;

import game.Interlude;
import game.Orientation;
import game.InstrumentType;
import game.Renderable;
import music.Handedness;
import music.SoundElement;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import util.Pair;

public class MovingSound implements Renderable {
    private final static float SPEED = 1.0f/3.5f;
    private float fractionX;
    private float fractionY;
    private int radius;
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
        g.setColor(Color.black);
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius ); 
    }
    
    public float fraction() {
        return fractionX;
    }
    
    public void update(int t) {
        fractionX += (SPEED * t) / 1000;
    }
    
    public void init() {
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        radius = containerHeight / 45;
    }
    
    public boolean offScreen() {
        return fractionX > 1;
    }
    
    public Handedness handedness() {
        return handedness;
    }
}
