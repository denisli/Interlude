package game;

import util.Pair;

/**
 * Orientation represents the orientation of the rounds in Interlude game. This will
 * determine where the note markers are located and also where the moving images will
 * come from.
 */
public class Orientation {
    /** Angle, in radians, of the orientation. The angle is counter-clockwise. */
    private static double currentAngle = 0; // must have a value of 0, pi/2, pi, or 3pi/2
    
    /**
     * Gets the position assuming that the screen has been rotated by currentAngle radians 
     * counter-clockwise around the center. 
     * @param fractionX
     * @param fractionY
     * @return
     */
    public static Pair<Float,Float> getPosition( float fractionX, float fractionY ) {
        float shiftedX = fractionX - 0.5f;
        float shiftedY = fractionY - 0.5f;
        float cosine = (float) Math.cos( currentAngle );
        float sine = (float) Math.sin( currentAngle );
        System.out.println(currentAngle);
        System.out.println(cosine + ", " + sine);
        float newFractionX = (float) ( cosine * shiftedX + sine * shiftedY ) + 0.5f;
        float newFractionY = (float) ( -sine * shiftedX + cosine * shiftedY ) + 0.5f;
        
        return new Pair<Float,Float>(newFractionX, newFractionY);
    }
    
    /**
     * Rotates the orientation counter-clockwise by 90 degrees
     */
    public static void rotateCounterClockwise() {
        currentAngle += Math.PI / 2;
        while (currentAngle >= 2 * Math.PI) {
            currentAngle -= 2 * Math.PI;
        }
    }
    
    /**
     * Rotates the orientation clockwise by 90 degrees
     */
    public static void rotateClockwise() {
        currentAngle -= Math.PI / 2;
        while (currentAngle < 0) {
            currentAngle += 2 * Math.PI;
        }
    }
}
