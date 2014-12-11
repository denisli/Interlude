package music;

public class Rest implements MusicElement {
    private final float durationType;
    private final int tempo;
    
    public Rest(float durationType, int tempo) {
        this.durationType = durationType;
        this.tempo = tempo;
    }
    
    @Override
    public int duration() {
        final int standard = 60000;
        return (int) Math.floor( standard * durationType / tempo );
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
