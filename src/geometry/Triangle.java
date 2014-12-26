package geometry;

import org.newdawn.slick.geom.Polygon;

public class Triangle extends Polygon {
    /**
     * 
     */
    private static final long serialVersionUID = 3316291376890092076L;

    public Triangle( float x1, float y1, float x2, float y2, float x3, float y3 ) {
        super( new float[] { x1, y1, x2, y2, x3, y3 } );
    }
}
