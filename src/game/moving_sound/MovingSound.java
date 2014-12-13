package game.moving_sound;

import game.Interlude;
import game.VoiceType;
import game.Renderable;
import music.Note;
import music.Simultaneous;
import music.SoundElement;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MovingSound implements Renderable {
    private final static int SPEED = 10;
    private static final int NUM_LETTERS = 8; // number of letters to represent sound elements
    private int centerX;
    private int centerY;
    private int radius;
    private VoiceType voiceType;
    
    private final SoundElement soundElement;
    
    public MovingSound( SoundElement soundElement, VoiceType voiceType ) {
        this.soundElement = soundElement;
        this.voiceType = voiceType;
    }
    
    public SoundElement soundElement() {
        return soundElement;
    }
    
    public void render(Graphics g) {
        g.setColor(Color.white);
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius ); 
    }
    
    public void update(int t) {
        if ( voiceType == VoiceType.SINGLE ) {
            centerX -= SPEED * t / 10;
        } else {
            centerY += SPEED * t / 10;
        }
    }
    
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        if ( voiceType == VoiceType.SINGLE) {
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
        } else {
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
            if ( voiceType == VoiceType.RIGHT ) {
                centerX += NUM_LETTERS * increment;
            }
            
            radius = containerHeight / 45;
        }
    }
    
    public boolean offScreen() {
        if ( voiceType == VoiceType.SINGLE ) {
            return centerX + radius < 0;
        } else {
            return centerY + radius > Interlude.GAME_CONTAINER.getHeight();
        }
    }
    
    public VoiceType voiceType() {
        return voiceType;
    }
}
