package game.fonts;

import java.awt.Font;

import org.newdawn.slick.UnicodeFont;

public class GameFonts {
    public static final UnicodeFont ARIAL_PLAIN_18 = SimpleFont.retrieve("Arial", Font.PLAIN, 18);
    public static final UnicodeFont ARIAL_PLAIN_24 = SimpleFont.retrieve("Arial", Font.PLAIN, 24);
    public static final UnicodeFont ARIAL_PLAIN_36 = SimpleFont.retrieve("Arial", Font.PLAIN, 36);
    public static final UnicodeFont ARIAL_PLAIN_32 = SimpleFont.retrieve("Arial", Font.PLAIN, 32);
    public static final UnicodeFont ARIAL_PLAIN_54 = SimpleFont.retrieve("Arial", Font.PLAIN, 54);
    
    public static void loadFonts() {
        // initializes the static variables simply from calling this
    }
}
