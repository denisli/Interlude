package game;

import util.Pair;

public class Reflection {
    private static boolean horizontalMirrored = false;
    private static boolean verticalMirrored = false;
    
    public static Pair<Float,Float> getPosition( float fractionX, float fractionY ) {
        if ( verticalMirrored ) {
            fractionY = 1 - fractionY;
        } else if ( horizontalMirrored ) {
            fractionX = 1 - fractionX;
        }
        return new Pair<Float,Float>(fractionX, fractionY);
    }
    
    public static Pair<Float,Float> getPosition( Pair<Float,Float> pair ) {
        float fractionX = pair.getLeft();
        float fractionY = pair.getRight();
        return getPosition( fractionX, fractionY );
    }
    
    public static void verticallyMirror() {
        verticalMirrored = !verticalMirrored;
    }
    
    public static void horizontallyMirror() {
        horizontalMirrored = !horizontalMirrored;
    }
}
