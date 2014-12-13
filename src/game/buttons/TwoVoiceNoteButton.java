package game.buttons;

import java.awt.Font;

import game.Controls;
import game.Hand;
import game.Interlude;
import game.InterludeGame;
import music.Note;
import music.Simultaneous;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class TwoVoiceNoteButton extends NoteButton {
    private static final int NUM_LETTERS = 8; // number of letters to represent sound elements
    
    private final Hand hand;
    private int centerX;
    private int centerY;
    private int radius;
    private UnicodeFont font;
    private int key;
    
    public TwoVoiceNoteButton( int note, Hand hand ) {
        super(note);
        this.hand = hand;
        this.key = Controls.correspondingKey( note, hand );
    }

    @Override
    public void render(Graphics g) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        if (input.isKeyDown( key )) {
            g.setColor(Color.blue);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius); 
        g.setColor(Color.red);
        g.setFont( font );
        g.drawString( Note.toStringLetter( note() ), centerX, centerY);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
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
        if ( hand == Hand.RIGHT ) {
            centerX += NUM_LETTERS * increment;
        }
        
        radius = increment * 2 / 5;
        
        try {
            font = (new SimpleFont( "Arial", Font.PLAIN, 36 )).get();
        } catch (SlickException se) {
            se.printStackTrace();
        }
    }

    @Override
    public int width() {
        return 2 * radius;
    }

    @Override
    public int height() {
        return 2 * radius;
    }
}
