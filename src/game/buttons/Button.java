package game.buttons;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public interface Button {
    public static Button playButton() {
        return new PlayButton();
    }
    
    public static Button songSelectionButton(String songTitle, int yCoord) {
        return new SongSelectionButton(songTitle, yCoord);
    }
    
    public static Button noteButton(int note) {
        return new NoteButton(note);
    }
    
    public void render(GameContainer gc, Graphics g);
    
    public void update(GameContainer gc, int t);
    
    public void init(GameContainer gc);
    
    public boolean isClicked(Input input);
    
    public int width();
    
    public int height();
}
