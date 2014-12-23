package game.buttons;

import game.Interlude;
import game.SimpleFont;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class TextButton implements Button {
    private final String text;
    private Color color = Color.black;
    private UnicodeFont font;
    private Rectangle boundingBox;
    private boolean mouseWasDown;
    private final float fractionX;
    private final float fractionY;
    private Runnable effect;
    
    public TextButton( String text, float fractionX, float fractionY, Runnable effect) {
        this.text = text;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.effect = effect;
    }
    
    public TextButton( String text, float fractionX, float fractionY ) {
        this(text, fractionX, fractionY, (Runnable) () -> {});
    }
    
    @Override
    public void render(Graphics g) {
        g.setFont( font );
        g.setColor( color );
        g.drawString( text, boundingBox.getX(), boundingBox.getY());
    }
    
    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        float mouseX = input.getMouseX();
        float mouseY = input.getMouseY();
        if (boundingBox.contains( mouseX, mouseY )) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                mouseWasDown = true;
                color = Color.red;
            } else {
                color = Color.green;
            }
        } else {
            mouseWasDown = false;
            color = Color.black;
        }
        if ( isClicked(input) ) {
            callEffect();
        }
    }
    
    @Override
    public void init() {
        this.font = getFont();
        this.boundingBox = boundingBox();
        mouseWasDown = false;
    }
    
    private UnicodeFont getFont() {
        try {
            return (new SimpleFont( "Arial", Font.PLAIN, 36 )).get();
        } catch (SlickException se) {
            return null;
        }
    }
    
    private Rectangle boundingBox() {;
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return getFont().getWidth(text);
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return getFont().getHeight(text);
    }

    @Override
    public boolean isClicked(Input input) {
        // TODO Auto-generated method stub
        return mouseWasDown && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
    }
    
    @Override
    public void setEffect(Runnable effect) {
        this.effect = effect;
    }
    
    @Override
    public void callEffect() {
        effect.run();
    }
}
