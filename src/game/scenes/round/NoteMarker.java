package game.scenes.round;

import java.util.List;

import game.Interlude;
import game.fonts.GameFonts;
import game.settings.Controls;
import game.settings.Handedness;
import game.settings.Orientation;
import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

import util.Pair;

public class NoteMarker {
    public static final float FRACTION_X = 0.85f;
    private static final List<Integer> NOTES_IN_ORDER = Controls.notesInOrder();
    private final int note;
    private final Handedness handedness;
    private float fractionY;
    private int radius;
    private UnicodeFont font;
    private final int key;
    private Color color = Color.yellow;
    private DisplayType displayType = DisplayType.KEY;
    
    public NoteMarker( int note, Handedness handedness ) {
        this.note = note;
        this.handedness = handedness;
        this.key = Controls.correspondingKey( note, handedness );
    }
    
    public int note() {
        return note;
    }
    
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        Pair<Float,Float> actualPosition = Orientation.getPosition( FRACTION_X, fractionY );
        float actualFractionX = actualPosition.getLeft();
        float actualFractionY = actualPosition.getRight();
        int centerX = (int) (actualFractionX * containerWidth);
        int centerY = (int) (actualFractionY * containerHeight);
        g.setColor( color );
        g.fillOval( centerX - radius, centerY - radius, 2 * radius, 2 * radius); 
        g.setColor( color );
        
        Pair<Float,Float> otherEndPosition = Orientation.getPosition( 0, fractionY );
        float otherEndFractionX = otherEndPosition.getLeft();
        float otherEndFractionY = otherEndPosition.getRight();
        int otherEndCenterX = (int) (otherEndFractionX * containerWidth);
        int otherEndCenterY = (int) (otherEndFractionY * containerHeight);
        g.drawLine( centerX, centerY, otherEndCenterX, otherEndCenterY);
        
        g.setColor( Color.black );
        g.setFont( font );
        
        String noteText = null;
        if ( displayType == DisplayType.LETTER ) {
            noteText = Note.toLetter(note());
        } else if (displayType == DisplayType.KEY ){
            noteText = Input.getKeyName( Controls.correspondingKey( note(), handedness ) );
            if ( noteText.equals("SEMICOLON") ) {
                noteText = ";";
            }
        }
        
        int textWidth = font.getWidth(noteText);
        int textHeight = font.getHeight(noteText);
        g.drawString( noteText, centerX  - textWidth / 2, centerY - textHeight / 2);
    }
    
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        if (input.isKeyDown( key )) {
            color = Color.darkGray;
        } else {
            color = Color.lightGray;
        };
    }
    
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int numLetters = NOTES_IN_ORDER.size();
        if ( handedness == Handedness.SINGLE ) {
            float initial = 0.2f;
            float increment = (1.0f - 2 * initial) / (numLetters - 1);
            
            for ( int i = 0; i < numLetters; i++ ) {
                if ( note == NOTES_IN_ORDER.get(i) ) {
                    fractionY = initial + i * increment;
                }
            }
            radius = (int) (Math.min(containerWidth, containerHeight) * increment * 2) / 5;
        } else {
            float initial = 0.1f;
            float increment = (1.0f - 2 * initial) / (2 * numLetters - 1);
            for ( int i = 0; i < NOTES_IN_ORDER.size(); i++ ) {
                if ( note == NOTES_IN_ORDER.get(i) ) {
                    fractionY = initial + i * increment;
                }
            }
            if ( handedness == Handedness.RIGHT ) {
                fractionY += numLetters * increment;
            }
            radius = (int) (Math.min(containerWidth, containerHeight) * increment * 2) / 5;
        }
        
        font = GameFonts.ARIAL_PLAIN_36;
    }
    
    public float fractionY() {
        return fractionY;
    }
    
    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }
}
