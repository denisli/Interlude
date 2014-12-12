package game;

import music.SoundElement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class MovingSound {
    protected static final int SPEED = 10;
    
    private final SoundElement soundElement;
    
    public MovingSound( SoundElement soundElement ) {
        this.soundElement = soundElement;
    }
    
    public SoundElement soundElement() {
        return soundElement;
    }
    
    public abstract void render(GameContainer gc, Graphics g);
    
    public abstract void update(GameContainer gc, int t);
    
    public abstract void init(GameContainer gc);
    
    public abstract boolean offScreen();
}
