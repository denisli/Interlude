package game.buttons;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class SongSelectionButton implements Button {
    private final String songTitle;
    private Color color = Color.yellow;
    private UnicodeFont font;
    private Rectangle boundingBox;
    private final int yCoord;

    public SongSelectionButton(String songTitle, int yCoord) {
        this.songTitle = songTitle;
        this.yCoord = yCoord;
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        g.setFont( font );
        g.setColor( color );
        g.drawString( songTitle, boundingBox.getX(), boundingBox.getY());
    }

    @Override
    public void update(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        Input input = gc.getInput();
        float mouseX = input.getMouseX();
        float mouseY = input.getMouseY();
        if (boundingBox.contains( mouseX, mouseY )) {
            if (input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)) {
                color = Color.red;
            } else {
                color = Color.green;
            }
        } else {
            color = Color.yellow;
        }
    }

    @Override
    public void init(GameContainer gc) {
        // TODO Auto-generated method stub
        this.font = getFont();
        this.boundingBox = boundingBox(gc);
    }
    
    private UnicodeFont getFont() {
        try {
            return (new SimpleFont( "Arial", Font.PLAIN, 36 )).get();
        } catch (SlickException se) {
            return null;
        }
    }
    
    private Rectangle boundingBox(GameContainer gc) {;
        int width = font.getWidth(songTitle);
        int height = font.getHeight(songTitle);
        int containerWidth = gc.getWidth();
        int containerHeight = gc.getHeight();
        return new Rectangle( (containerWidth - width)/2, containerHeight/10 + yCoord, width, height );
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return getFont().getWidth(songTitle);
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return getFont().getHeight(songTitle);
    }
}