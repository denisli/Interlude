package game.scrollpane;

import game.Renderable;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

public class ScrollBar extends RoundedRectangle implements Renderable {
    private final ScrollPane scrollPane;
    
    /**
     * Required serialization ID
     */
    private static final long serialVersionUID = 1L;
    
    public ScrollBar( ScrollPane scrollPane, float x, float y, float width, float height, float cornerRadius) {
        super(x, y, width, height, cornerRadius);
        this.scrollPane = scrollPane;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }
    
    

}
