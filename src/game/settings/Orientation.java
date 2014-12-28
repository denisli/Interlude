package game.settings;

import util.Pair;

/**
 * Orientation represents the orientation of the rounds in Interlude game. This will
 * determine where the note markers are located and also where the moving images will
 * come from.
 */
public class Orientation {
    /** Angle, in degrees, of the orientation. The angle is counter-clockwise. */
    private static double currentAngle = 0; // must have a value of 0, 90, 180, or 270
    
    /**
     * Gets the position assuming that the screen has been rotated by currentAngle degrees 
     * counter-clockwise around the center. 
     * @param fractionX
     * @param fractionY
     * @return
     */
    public static Pair<Float,Float> getPosition( float fractionX, float fractionY ) {
        if ( currentAngle == 0 ) {
            return new Pair<Float,Float>(fractionX,fractionY);
        } else if ( currentAngle == 90 ) {
            return new Pair<Float,Float>(fractionY, 1-fractionX);
        } else if ( currentAngle == 180 ) {
            return new Pair<Float,Float>(1-fractionX, fractionY);
        } else if ( currentAngle == 270 ) {
            return new Pair<Float,Float>(fractionY, fractionX);
        } else {
            throw new RuntimeException("You must've messed up implementing something bro");
        }
    }
    
    /**
     * Rotates the orientation counter-clockwise by 90 degrees
     */
    public static void rotateCounterClockwise() {
        currentAngle += 90;
        while (currentAngle >= 360) {
            currentAngle -= 360;
        }
    }
    
    /**
     * Rotates the orientation clockwise by 90 degrees
     */
    public static void rotateClockwise() {
        currentAngle -= 90;
        while (currentAngle < 0) {
            currentAngle += 360;
        }
    }
}
