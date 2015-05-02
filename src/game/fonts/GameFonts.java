package game.fonts;

import game.Interlude;

import java.awt.Font;
import java.util.function.Function;

import org.newdawn.slick.UnicodeFont;

public class GameFonts {
	private static final double MY_HEIGHT = 1080.0;
	private static final double FACTOR = Interlude.GAME_CONTAINER.getHeight() / MY_HEIGHT;
	private static final String FONT_NAME = "Calibri";
	
    public static final UnicodeFont ARIAL_PLAIN_14 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (14 * FACTOR));
    public static final UnicodeFont ARIAL_PLAIN_18 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (18 * FACTOR));
    public static final UnicodeFont ARIAL_PLAIN_24 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (24 * FACTOR));
    public static final UnicodeFont ARIAL_PLAIN_36 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (36 * FACTOR));
    public static final UnicodeFont ARIAL_PLAIN_32 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (32 * FACTOR));
    public static final UnicodeFont ARIAL_PLAIN_54 = SimpleFont.retrieve(FONT_NAME, Font.PLAIN, (int) (54 * FACTOR));
    
    public static void loadFonts() {
        // initializes the static variables simply from calling this
    }
}
