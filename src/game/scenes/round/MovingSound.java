package game.scenes.round;

import game.GameObject;
import game.Interlude;
import game.settings.Handedness;
import game.settings.Orientation;
import music.Instrument;
import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import util.Pair;

public class MovingSound implements GameObject {
    public final static float SPEED = 1.0f/3500.0f; // how much of the screen in moves in 1 millisecond
    private float fractionX;
    private float fractionY;
    private int radius;
    private Color color = Color.orange;
    private Handedness handedness;
    
    private final Note note;
    
    public MovingSound( float fractionX, float fractionY, Note name, Handedness handedness ) {
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.note = name;
        this.handedness = handedness;
    }
    
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        Pair<Float,Float> actualPosition = Orientation.getPosition( fractionX, fractionY );
        float actualFractionX = actualPosition.getLeft();
        float actualFractionY = actualPosition.getRight();
        int centerX = (int) ( actualFractionX * containerWidth );
        int centerY = (int) ( actualFractionY * containerHeight );
        Shape shape = new Circle(centerX, centerY, radius);
        g.setColor(color);
        g.fill(shape);
        g.setColor(Color.black);
        g.draw(shape); 
    }
    
    public void bePlayed(Instrument instrument) {
    	note.bePlayed(instrument);
    }
    
    public int duration() {
    	return note.duration();
    }
    
    public Note note() {
    	return note;
    }
    
    public float fraction() {
        return fractionX;
    }
    
    public void update(int t) {
        fractionX += SPEED * t;
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
