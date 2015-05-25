package music;

public interface Instrument {
    public static InstrumentType typeOfInstrument( int programNumber ) {
        if ( programNumber == 0 ) {
            return InstrumentType.DOUBLE;
        } else {
            return InstrumentType.SINGLE;
        }
    }
    
    public void setVolumeRatio(double volumeRatio);
    
    public void update(int t);
    
    public void play(Note note);
    
    public void pause();
    
    public void resume();
        
    public String getInstrumentName();
    
    public int getProgram();
    
    public InstrumentType type();
    
    public void clear();
    
    @Override
    public boolean equals(Object other);
    
    @Override
    public int hashCode();
}
