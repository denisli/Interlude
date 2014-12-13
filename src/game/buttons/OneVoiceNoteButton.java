package game.buttons;

import game.Hand;
import game.Controls;
import game.Interlude;
import game.InterludeGame;

import java.awt.Font;

import music.Note;
import music.Simultaneous;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class OneVoiceNoteButton extends NoteButton {
    private int centerX;
    private int centerY;
    private int radius;
    private UnicodeFont font;
    private int key;
    
    public OneVoiceNoteButton( int note ) {
        super( note );
        this.key = Controls.correspondingKey( note );
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
        return;
    }
    
    @Override
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
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
