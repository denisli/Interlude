package music;

public class Rest extends MusicElement {
    private final float durationType;
    private final int tempo;
    
    public Rest(float durationType, int tempo) {
        this.durationType = durationType;
        this.tempo = tempo;
    }
    
    public int duration() {
        final int standard = 60000; // need a better name for this...?
        return (int) Math.floor( standard * durationType / tempo );
    }
}
