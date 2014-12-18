package game.note_marker;

import java.awt.Font;

import game.Controls;
import game.Interlude;
import game.Orientation;
import game.Reflection;
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

import util.Pair;

public class NoteMarker {
    private static final int NUM_LETTERS = 8; // number of letters to represent sound elements
    private final int note;
    private Runnable effect = (Runnable) () -> {};
    private final VoiceType voiceType;
    private float fractionX;
    private float fractionY;
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
    
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        Pair<Float,Float> actualPosition = Reflection.getPosition(Orientation.getPosition( fractionX, fractionY ));
        float actualFractionX = actualPosition.getLeft();
        float actualFractionY = actualPosition.getRight();
        int centerX = (int) (actualFractionX * containerWidth);
        int centerY = (int) (actualFractionY * containerHeight);
        g.setColor( color );
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius); 
        g.setColor( Color.red );
        g.setFont( font );
        String noteText = Input.getKeyName( Controls.correspondingKey( note() , voiceType) );
        int textWidth = font.getWidth(noteText);
        int textHeight = font.getHeight(noteText);
        g.drawString( noteText, centerX  - textWidth / 2, centerY - textHeight / 2);
    }
    
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        if (input.isKeyDown( key )) {
            color = Color.blue;
        } else {
            color = Color.yellow;
        };
    }
    
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        if ( voiceType == VoiceType.SINGLE ) {
            float increment = 0.1f;
            fractionX = 0.9f;
            if (note() == Note.A) {
                fractionY = increment;
            } else if (note() == Note.B) {
                fractionY = 2 * increment;
            } else if (note() == Note.C) {
                fractionY = 3 * increment;
            } else if (note() == Note.D) {
                fractionY = 4 * increment;
            } else if (note() == Note.E) {
                fractionY = 5 * increment;
            } else if (note() == Note.F) {
                fractionY = 6 * increment;
            } else if (note() == Note.G) {
                fractionY = 7 * increment;
            } else if (note() == Simultaneous.S) {
                fractionY = 8 * increment;
            } else {
                throw new IllegalArgumentException("Note button not given a valid note to represent");
            }
            radius = (int) (Math.min(containerWidth, containerHeight) * increment * 2) / 5;
        } else {
            float increment = 0.05f;
            fractionX = 0.9f;
            if (note() == Note.A) {
                fractionY = 2 * increment;
            } else if (note() == Note.B) {
                fractionY = 3 * increment;
            } else if (note() == Note.C) {
                fractionY = 4 * increment;
            } else if (note() == Note.D) {
                fractionY = 5 * increment;
            } else if (note() == Note.E) {
                fractionY = 6 * increment;
            } else if (note() == Note.F) {
                fractionY = 7 * increment;
            } else if (note() == Note.G) {
                fractionY = 8 * increment;
            } else if (note() == Simultaneous.S) {
                fractionY = 9 * increment;
            } else {
                throw new IllegalArgumentException("Note button not given a valid note to represent");
            }
            if ( voiceType == VoiceType.RIGHT ) {
                fractionY += NUM_LETTERS * increment;
            }
            radius = (int) (Math.min(containerWidth, containerHeight) * increment * 2) / 5;
        }
        
        try {
            font = (new SimpleFont( "Arial", Font.PLAIN, 36 )).get();
        } catch (SlickException se) {
            se.printStackTrace();
        }
    }
    
    public float fractionX() {
        return fractionX;
    }
    
    public float fractionY() {
        return fractionY;
    }
}
