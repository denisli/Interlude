package music;

import java.util.ArrayList;
import java.util.List;

public class Simultaneous implements SoundElement {
    public static final int S = 128;
    
    private final List<SoundElement> elements;
    
    public Simultaneous(List<SoundElement> elements) {
        this.elements = elements;
    }
    
    // not the actual duration. Maybe I should change it to say minDuration
    @Override
    public int duration() {
        int maxNoteDuration = 0;
        for (SoundElement element : elements) {
            maxNoteDuration = Math.max(maxNoteDuration, element.duration());
        }
        return maxNoteDuration;
    }
    
    public List<SoundElement> soundElements() {
        return elements;
    }
    
    @Override
    public int integer() {
        return Simultaneous.S;
    }

    @Override
    public void bePlayed(Instrument instrument) {
        instrument.play( this );
    }
    
    @Override
    public SoundElement correspondingSoundElement(int letter) {
        if (letter == Simultaneous.S) {
            return this;
        }
        List<SoundElement> correspondingElements = new ArrayList<SoundElement>();
        for ( SoundElement element : elements ) {
            correspondingElements.add( element.correspondingSoundElement(letter) );
        }
        return new Simultaneous( correspondingElements );
    }
}
