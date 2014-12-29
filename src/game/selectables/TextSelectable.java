package game.selectables;

import game.Interlude;
import game.fonts.GameFonts;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class TextSelectable extends Selectable {
    private final String text;
    private final UnicodeFont font = GameFonts.ARIAL_PLAIN_36;
    private final float fractionX;
    private final float fractionY;
    private Rectangle boundingBox;
    private boolean mouseWasDown = false;
    private Color color = Color.black;

    public TextSelectable( String text, float fractionX, float fractionY, Statement condition, Runnable effect ) {
        super( condition, effect );
        this.text = text;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
    }
    
    @Override
    public void render(Graphics g) {
        g.setColor( color );
        g.setFont( font );
        g.drawString( text, boundingBox.getX(), boundingBox.getY());
    }
    
    @Override
    public void update(int t) {
        super.update(t);
        Input input = Interlude.GAME_CONTAINER.getInput();
        float mouseX = input.getMouseX();
        float mouseY = input.getMouseY();
        
        if ( selected() ) {
            color = Color.red;
        } else {
            color = Color.black;
        }
        
        if (boundingBox.contains( mouseX, mouseY )) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                mouseWasDown = true;
                color = Color.orange;
            } else {
                color = Color.green;
            }
        } else {
            mouseWasDown = false;
        }
    }

    @Override
    public void init() {
        this.boundingBox = boundingBox();
    }
    
    private Rectangle boundingBox() {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
    }

    @Override
    protected boolean isClicked(Input input) {
        boolean isClicked = mouseWasDown && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        if ( isClicked ) {
            mouseWasDown = false;
        }
        return isClicked;
    }
}
