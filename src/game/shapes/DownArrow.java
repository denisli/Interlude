package game.shapes;

import game.Interlude;
import game.Renderable;
import geometry.Triangle;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DownArrow extends Shape {
    private final Triangle triangle;
    private final Color color;
    
    /**
     * Constructor for a DownArrow. The shape of a DownArrow is a triangle with horizontal symmetry.
     * @param fractionX
     *          where the horizontal center of the arrow is, expressed as a fraction of the 
     *          game container width
     * @param fractionY
     *          where the highest vertical point of the arrow is, expressed as a fraction of the
     *          game container height
     * @param color
     *          the color of the arrow
     */
    public DownArrow( float fractionX, float fractionY, Color color ) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int centerX = (int) ( fractionX * containerWidth );
        int highestY = (int) ( fractionY * containerHeight );
        this.triangle = new Triangle( centerX - 25, highestY, centerX, highestY + 25, centerX + 25, highestY );
        this.color = color;
    }
    
    public DownArrow( float fractionX, float fractionY ) {
        this( fractionX, fractionY, Color.black );
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(color);
        g.draw(triangle);
        g.fill(triangle);
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
