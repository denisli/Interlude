package game.scrollpane;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

import game.Renderable;

public class ScrollPane implements Renderable {
    private final float fractionX;
    private final float fractionY;
    private final float fractionWidth;
    private final float fractionHeight;
    private final List<Renderable> items;
    private ScrollBar scrollBar;
    
    public ScrollPane( float fractionX, float fractionY, float fractionWidth, float fractionHeight, List<Renderable> items ) {
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.fractionWidth = fractionWidth;
        this.fractionHeight = fractionHeight;
        this.items = items;
    }
    
    public ScrollPane( float fractionX, float fractionY, float fractionWidth, float fractionHeight ) {
        this( fractionX, fractionY, fractionWidth, fractionHeight, new ArrayList<Renderable>() );
    }
    
    public ScrollPane( float fractionWidth, float fractionHeight, List<Renderable> items ) {
        this( 0.5f, 0.5f, fractionWidth, fractionHeight, items );
    }
    
    public ScrollPane( float fractionWidth, float fractionHeight ) {
        this( fractionWidth, fractionHeight, new ArrayList<Renderable>() );
    }
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( Renderable item : items ) {
            item.render(g);
        }
        
        scrollBar.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for ( Renderable item : items ) {
            item.update(t);
        }
        
        scrollBar.update(t);
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        for ( Renderable item : items ) {
            item.init();
        }
        
        scrollBar.init();
    }
    
    public void scrollUp() {
        
    }

}
