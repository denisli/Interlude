package game.note_marker;

import java.awt.Font;

import game.Controls;
import game.Interlude;
import game.SimpleFont;
import game.VoiceType;
import game.buttons.Button;
import music.Note;
import music.Simultaneous;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class NoteMarker implements Button {
    private static final int NUM_LETTERS = 8; // number of letters to represent sound elements
    private final int note;
    private Runnable effect = (Runnable) () -> {};
    private final VoiceType voiceType;
    private int centerX;
    private int centerY;
    private int radius;
    private UnicodeFont font;
    private final int key;
    private Color color = Color.yellow;
    
    public NoteMarker( int note, VoiceType voiceType ) {
        this.note = note;
        this.voiceType = voiceType;
        this.key = Controls.correspondingKey( note, voiceType );
    }
    
    public int note() {
        return note;
    }

    @Override
    public boolean isClicked(Input input) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void setEffect(Runnable effect) {
        this.effect = effect;
    }
    
    @Override
    public void callEffect() {
        effect.run();
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor( color );
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius); 
        g.setColor( Color.red );
        g.setFont( font );
        g.drawString( Note.toStringLetter( note() ), centerX, centerY);
    }
    
    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        if (input.isKeyDown( key )) {
            color = Color.blue;
        } else {
            color = Color.yellow;
        };
    }
    
    @Override
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        if ( voiceType == VoiceType.SINGLE ) {
            int increment = containerHeight / 10;
            centerX = containerWidth / 10;
            if (note() == Note.A) {
                centerY = increment;
            } else if (note() == Note.B) {
                centerY = 2 * increment;
            } else if (note() == Note.C) {
                centerY = 3 * increment;
            } else if (note() == Note.D) {
                centerY = 4 * increment;
            } else if (note() == Note.E) {
                centerY = 5 * increment;
            } else if (note() == Note.F) {
                centerY = 6 * increment;
            } else if (note() == Note.G) {
                centerY = 7 * increment;
            } else if (note() == Simultaneous.S) {
                centerY = 8 * increment;
            } else {
                throw new IllegalArgumentException("Note button not given a valid note to represent");
            }
            radius = containerHeight / 40;
        } else {
            int increment = containerWidth / 20;
            centerY = 9 * containerHeight / 10;
            if (note() == Note.A) {
                centerX = 2 * increment;
            } else if (note() == Note.B) {
                centerX = 3 * increment;
            } else if (note() == Note.C) {
                centerX = 4 * increment;
            } else if (note() == Note.D) {
                centerX = 5 * increment;
            } else if (note() == Note.E) {
                centerX = 6 * increment;
            } else if (note() == Note.F) {
                centerX = 7 * increment;
            } else if (note() == Note.G) {
                centerX = 8 * increment;
            } else if (note() == Simultaneous.S) {
                centerX = 9 * increment;
            } else {
                throw new IllegalArgumentException("Note button not given a valid note to represent");
            }
            if ( voiceType == VoiceType.RIGHT ) {
                centerX += NUM_LETTERS * increment;
            }
            
            radius = increment * 2 / 5;
        }
        
        try {
            font = (new SimpleFont( "Arial", Font.PLAIN, 36 )).get();
        } catch (SlickException se) {
            se.printStackTrace();
        }
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return 2 * radius;
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return 2 * radius;
    }
}
