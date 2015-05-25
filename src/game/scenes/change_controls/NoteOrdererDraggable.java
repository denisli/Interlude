package game.scenes.change_controls;

import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import game.Interlude;
import game.Renderable;
import game.Updateable;
import game.fonts.GameFonts;

public class NoteOrdererDraggable implements Renderable, Updateable {
    private float fractionX;
    private float fractionY;
    private final String noteLetter;
    private final UnicodeFont font = GameFonts.ARIAL_PLAIN_36;
    private Rectangle boundingBox;
    
    private Color color = Color.black;
    private boolean mouseWasPressed = false;
    private boolean mouseWasDown = false;
    
    public static NoteOrdererDraggable draggable( int noteInteger, float fractionX, float fractionY ) {
        NoteOrdererDraggable draggable = new NoteOrdererDraggable( noteInteger, fractionX, fractionY );
        draggable.init();
        return draggable;
    }
    
    public static NoteOrdererDraggable draggable( String noteLetter, float fractionX, float fractionY ) {
        NoteOrdererDraggable draggable = new NoteOrdererDraggable( noteLetter, fractionX, fractionY );
        draggable.init();
        return draggable;
    }
    
    public NoteOrdererDraggable( int noteInteger, float fractionX, float fractionY ) {
        this.noteLetter = Note.toLetter(noteInteger);
        this.fractionX = fractionX;
        this.fractionY = fractionY;
    }
    
    public NoteOrdererDraggable( String noteLetter, float fractionX, float fractionY ) {
        this.noteLetter = noteLetter;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
    }
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int centerX = (int) (fractionX * containerWidth);
        int centerY = (int) (fractionY * containerHeight);
        int textWidth = font.getWidth( noteLetter );
        int textHeight = font.getHeight( noteLetter );
        g.setFont( font );
        g.setColor( color );
        g.drawString( noteLetter, centerX - textWidth / 2, centerY - textHeight / 2);
    }

    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();

        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        
        if ( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) ) {
            if ( !mouseWasDown ) {
                if ( boundingBox.contains( mouseX, mouseY )) {
                    mouseWasPressed = true;
                }
            }
            
            if ( mouseWasPressed ) {
                fractionX = ((float) mouseX ) / Interlude.GAME_CONTAINER.getWidth();
                fractionY = ((float) mouseY ) / Interlude.GAME_CONTAINER.getHeight();
            }
            
            mouseWasDown = true;
        } else {
            if ( mouseWasDown && mouseWasPressed ) {
                boundingBox = boundingBox();
            }
            mouseWasDown = false;
            mouseWasPressed = false;
            color = Color.black;
            
            if ( boundingBox.contains(mouseX, mouseY) ) {
                color = Color.red;
            }
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        boundingBox = boundingBox();
    }
    
    public boolean isDragged( Input input ) {
        return mouseWasPressed && mouseWasDown;
    }
    
    public float getFractionX() {
        return fractionX;
    }
    
    public float getFractionY() {
        return fractionY;
    }
    
    public void setPosition( float fractionX, float fractionY ) {
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        boundingBox = boundingBox();
    }
    
    
    public boolean closeEnough( float fractionX, float fractionY ) {
        return Math.abs(this.fractionX - fractionX) < 0.02f && Math.abs(this.fractionY - fractionY) < 0.02f; 
    }
    
    private Rectangle boundingBox() {
        int width = font.getWidth( noteLetter );
        int height = font.getHeight( noteLetter );
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
    }
}
