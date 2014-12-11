package music;

public interface MusicElement {
    
    /**
     * @return time from the start of the music element until the end of the element
     */
    public int duration();
    
    /**
     * @return time until the next music element is played
     */
    public int timeUntilNextElement();
    
    public void bePlayed(Instrument instrument);
    
    public boolean isRest();
}
