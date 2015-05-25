package game.shapes;

import org.newdawn.slick.Color;

import game.Renderable;
import game.Updateable;

public abstract class Shape implements Renderable, Updateable {
    public static Shape downArrow( float fractionX, float fractionY, Color color) {
        Shape shape = new DownArrow( fractionX, fractionY, color );
        shape.init();
        return shape;
    }
    
    public static Shape downArrow( float fractionX, float fractionY ) {
        Shape shape = new DownArrow( fractionX, fractionY );
        shape.init();
        return shape;
    }
    
    public static Shape upArrow( float fractionX, float fractionY, Color color) {
        Shape shape = new UpArrow( fractionX, fractionY, color );
        shape.init();
        return shape;
    }
    
    public static Shape upArrow( float fractionX, float fractionY ) {
        Shape shape = new UpArrow( fractionX, fractionY );
        shape.init();
        return shape;
    }
}
