package game.buttons;

import game.Interlude;
import game.fonts.GameFonts;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class TwoFaceButton implements Button {
    private final String firstText;
    private final String secondText;
    private final Runnable firstEffect;
    private final Runnable secondEffect;
    
    private String text;
    private float fractionX;
    private float fractionY;
    private Color color = Color.black;
    private UnicodeFont font = GameFonts.ARIAL_PLAIN_36;
    private Rectangle boundingBox;
    private boolean mouseWasDown;
    private Runnable effect;
    
    public TwoFaceButton( String firstText, String secondText, float fractionX, float fractionY, Runnable firstEffect, Runnable secondEffect) {
        this.firstText = firstText;
        this.secondText = secondText;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.firstEffect = firstEffect;
        this.secondEffect = secondEffect;
        
        this.text = firstText;
        this.effect = firstEffect;
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
            if ( text.equals(firstText) ) {
                text = secondText;
                effect = secondEffect;
                boundingBox = boundingBox();
            } else {
                text = firstText;
                effect = firstEffect;
                boundingBox = boundingBox();
            }
        }
    }
    
    @Override
    public void init() {
        this.font = GameFonts.ARIAL_PLAIN_36;
        this.boundingBox = boundingBox();
        mouseWasDown = false;
    }
    
    private Rectangle boundingBox() {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
    }

    @Override
    public boolean isClicked(Input input) {
        boolean isClicked = mouseWasDown && !input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
        if ( isClicked ) {
            mouseWasDown = false;
        }
        return isClicked;
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
    public void moveLeft(float fractionX) {
        // TODO Auto-generated method stub
        this.fractionX -= fractionX;
        this.boundingBox = boundingBox();
    }

    @Override
    public void moveRight(float fractionX) {
        // TODO Auto-generated method stub
        this.fractionX += fractionX;
        this.boundingBox = boundingBox();
    }

    @Override
    public void moveDown(float fractionY) {
        // TODO Auto-generated method stub
        this.fractionY += fractionY;
        this.boundingBox = boundingBox();
    }

    @Override
    public void moveUp(float fractionY) {
        // TODO Auto-generated method stub
        this.fractionY -= fractionY;
        this.boundingBox = boundingBox();
    }
}
