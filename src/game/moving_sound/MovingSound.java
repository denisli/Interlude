package game.moving_sound;

import game.Hand;
import game.Renderable;
import music.SoundElement;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class MovingSound implements Renderable {
    protected static final int SPEED = 10;
    
    private final SoundElement soundElement;
    
    public MovingSound( SoundElement soundElement ) {
        this.soundElement = soundElement;
    }
    
    public SoundElement soundElement() {
        return soundElement;
    }
    
    public abstract boolean offScreen();
    
    public static MovingSound oneVoiceMovingSound( SoundElement soundElement ) {
        return new OneVoiceMovingSound( soundElement );
    }
    
    public static MovingSound twoVoiceMovingSound( SoundElement soundElement, Hand hand ) {
        return new TwoVoiceMovingSound( soundElement, hand );
    }
}
