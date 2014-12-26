package game.fonts;

/**
 * The code here can be found at http://slickrpg.blogspot.com/2011/07/unicode-font.html
 * All implementation (with the exception of getWidth() method) is done by Brandon Reid.
 */

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import java.awt.Color;
import java.awt.Font;
public class SimpleFont {
    private UnicodeFont font;
    public SimpleFont(String fontName, int style, int size, Color color) throws SlickException {
        this(new Font(fontName, style, size), color);
    }
    public SimpleFont(String fontName, int style, int size) throws SlickException {
        this(new Font(fontName, style, size));
    }
    public SimpleFont(Font font) throws SlickException {
        this(font, Color.white);
    }
    public SimpleFont(Font font, Color color) throws SlickException {
        this.font = new UnicodeFont(font);
        ColorEffect colorEffect = new ColorEffect(color);
        this.font.getEffects().add(colorEffect);
        this.font.addNeheGlyphs();
        this.font.loadGlyphs();
    }
    public void setColor(Color color) throws SlickException {
        font.getEffects().clear();
        font.getEffects().add(new ColorEffect(color));
        font.clearGlyphs();
        font.addNeheGlyphs();
        font.loadGlyphs();
    }
    public UnicodeFont get() {
        return font;
    }
    
    public static UnicodeFont retrieve( String fontName, int style, int size ) {
        try {
            return new SimpleFont( fontName, style, size ).get();
        } catch (SlickException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException("Do I ever get here? retrieve()");
        }
    }
}