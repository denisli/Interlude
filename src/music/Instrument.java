package music;

public interface Instrument {
    
    public void setVolumeRatio(double volumeRatio);
    
    public void update(int t);
    
    public void play(Note note);
    
    public void pause();
    
    public void resume();
        
    public String getInstrumentName();
    
    public void clear();
    
    @Override
    public boolean equals(Object other);
    
    @Override
    public int hashCode();
}
