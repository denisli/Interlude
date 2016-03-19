package game.scenes.round;

import game.GameObject;
import game.settings.Handedness;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

import music.Instrument;

public class StackedMovingSound implements GameObject {
	// stackedMovingSounds is a list of moving sounds
	private final List<MovingSound> stackedMovingSounds = new ArrayList<MovingSound>();
	private MovingSound longestMovingSound;
	
	public void addMovingSound(MovingSound movingSound) {
		// set the longest moving sound
		if ( stackedMovingSounds.isEmpty() ) {
			longestMovingSound = movingSound;
		} else {
			if ( longestMovingSound.duration() < movingSound.duration() ) {
				longestMovingSound = movingSound;
			}
		}
		// add to stack of moving sounds
		stackedMovingSounds.add(movingSound);
	}
	
	public void bePlayed(Instrument instrument) {
		for ( MovingSound movingSound : stackedMovingSounds ) {
			movingSound.bePlayed(instrument);
		}
	}
	
    public void render(Graphics g) {
        longestMovingSound.render(g);
    }
    
    public void update(int t) {
        longestMovingSound.update(t);
    }
    
    public void init() {
        
    }
    
    public float fraction() {
    	return longestMovingSound.fraction();
    }
    
    public boolean offScreen() {
        return longestMovingSound.offScreen();
    }
    
    public Handedness handedness() {
        return longestMovingSound.handedness();
    }
    
    public void setFront() {
        longestMovingSound.setFront();
    }
    
    public void setSecond() {
        longestMovingSound.setSecond();
    }
    
    public void setThird() {
        longestMovingSound.setThird();
    }
}
