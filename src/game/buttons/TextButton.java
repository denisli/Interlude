package game.buttons;

import game.Interlude;
import game.fonts.GameFonts;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class TextButton extends Button {
    private String text;
    private Color color = Color.black;
    private UnicodeFont font;
    private Rectangle boundingBox;
    private float fractionX;
    private float fractionY;
    
    public TextButton( String text, float fractionX, float fractionY, Runnable effect) {
        this.text = text;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.effect = effect;
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
        this.boundingShape = boundingBox;
    }
    
    private Rectangle boundingBox() {
        int width = font.getWidth(text);
        int height = font.getHeight(text);
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        return new Rectangle( ( (int) (fractionX * containerWidth) - width/2), (int) (fractionY * containerHeight) - height/2, width, height );
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
	public void clicking(Input input) {
		this.color = Color.red;
	}

	@Override
	void normalState(Input input) {
		this.color = Color.black;
	}
}
