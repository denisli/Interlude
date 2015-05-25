package game.buttons;

import game.Interlude;
import game.fonts.GameFonts;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class TwoFaceButton extends Button {
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
    public void init() {
        this.font = GameFonts.ARIAL_PLAIN_36;
        this.boundingBox = boundingBox();
        boundingShape = boundingBox;
    }
    
    private Rectangle boundingBox() {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
    }
    
    @Override
    public void clicking(Input input) {
    	this.color = Color.red;
    }
    
    @Override
    public void click(Input input) {
    	super.click(input);
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
    
    
    @Override
    public void setEffect(Runnable effect) {
        this.effect = effect;
    }

	@Override
	public void hover(Input input) {
		this.color = Color.green;
	}

	@Override
	void normalState(Input input) {
		this.color = Color.black;
	}
}
