package music;

public class Rest implements MusicElement {
    private final int duration;
    
    public Rest(float durationType, int tempo) {
        final int standard = 60000; // please get a better name
        this.duration = (int) Math.floor( standard * durationType / tempo );
    }
    
    public Rest(int duration) {
        this.duration = duration;
    }
    
    @Override
    public int duration() {
        return duration;
    }

    @Override
    public int timeUntilNextElement() {
        return duration();
    }
    
    @Override
    public void bePlayed(Instrument instrument) {
        instrument.play( this );
    }
    
    @Override
    public boolean isRest() {
        return true;
    }
}
