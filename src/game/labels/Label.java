package game.labels;

import java.awt.Font;

import music.Note;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

import game.Interlude;
import game.Renderable;
import game.fonts.GameFonts;
import game.fonts.SimpleFont;

public class Label implements Renderable {
    private String text;
    private float fractionX;
    private float fractionY;
    private final Color color;
    private final UnicodeFont font;
    private Rectangle boundingBox;
    private boolean mouseWasDown = false;
    private boolean wasDragged = false;
    
    public Label( String text, float fractionX, float fractionY, Color color, UnicodeFont font ) {
        this.text = text;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.color = color;
        this.font = font;
    }
    
    public static Label label(String text, float fractionX, float fractionY, Color color, UnicodeFont font) {
        Label label = new Label( text, fractionX, fractionY, color, font );
        label.init();
        return label;
    }
    
    public static Label scoreLabel( float fractionX, float fractionY, UnicodeFont font ) {
        Color color = Color.darkGray;
        Label label = new Label( "0", fractionX, fractionY, color, font );
        label.init();
        return label;
    }
    
    public static Label timeElapsedLabel() {
        float fractionX = 0.65f;
        float fractionY = 0.05f;
        Color color = Color.darkGray;
        UnicodeFont font = GameFonts.ARIAL_PLAIN_32;
        Label label = new Label( "0:00", fractionX, fractionY, color, font );
        label.init();
        return label;
    }
    
    public static Label endTimeLabel( int endTime ) {
        int endTimeInSeconds = endTime / 1000;
        int minutes = endTimeInSeconds / 60;
        int remainingSeconds = endTimeInSeconds % 60;
        float fractionX = 0.75f;
        float fractionY = 0.05f;
        Color color = Color.darkGray;
        UnicodeFont font = GameFonts.ARIAL_PLAIN_32;
        Label label = new Label( minutes + ":" + String.format("%02d",remainingSeconds), fractionX, fractionY, color, font );
        label.init();
        return label;
    }
    
    public static Label interludeLabel() {
        String text = "Interlude";
        float fractionX = 0.5f;
        float fractionY = 0.15f;
        Color color = Color.gray;
        UnicodeFont font = GameFonts.ARIAL_PLAIN_54;
        Label label = new Label( text, fractionX, fractionY, color, font );
        label.init();
        return label;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int textWidth = font.getWidth(text);
        int textHeight = font.getHeight(text);
        g.setFont(font);
        g.setColor(color);
        g.drawString( text, fractionX * containerWidth - textWidth / 2, fractionY * containerHeight - textHeight / 2 );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }
    
    public void setText( String text ) {
        this.text = text;
    }
}
