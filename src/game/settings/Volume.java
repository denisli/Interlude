package game.settings;

public class Volume {
    private final static double[] VOLUME_RATIOS = volumeRatiosInit();
    
    private static double[] volumeRatiosInit() {
        final int numberOfPrograms = 128;
        double[] volumeRatios = new double[numberOfPrograms];
        for ( int i = 0; i < numberOfPrograms; i++ ) {
            volumeRatios[i] = 1.0;
        }
        return volumeRatios;
    }
    
    public static double volumeRatio(int programNumber) {
        return VOLUME_RATIOS[programNumber];
    }
    
    public static void setVolumeRatio(int programNumber, double ratio) {
        VOLUME_RATIOS[programNumber] = ratio;
    }
    
    public static void reset() {
        for ( int i = 0; i < VOLUME_RATIOS.length; i++ ) {
            VOLUME_RATIOS[i] = 1;
        }
    }
}
