package music;

import java.util.List;

public class Simultaneous implements SoundElement {
    public static final int S = 128;
    
    private final List<MusicElement> elements;
    
    public Simultaneous(List<MusicElement> elements) {
        this.elements = elements;
    }
    
    // not the actual duration. Maybe I should change it to say minDuration
    @Override
    public int duration() {
        int maxNoteDuration = 0;
        for (MusicElement element : elements) {
            maxNoteDuration = Math.max(maxNoteDuration, element.duration());
        }
        return maxNoteDuration;
    }

    public int timeUntilNextElement() {
        return elements.get(0).timeUntilNextElement();
    }
    
    public List<MusicElement> musicElements() {
        return elements;
    }
    
    @Override
    public boolean isRest() {
        for ( MusicElement element : elements ) {
            if ( !element.isRest() ) {
                return false;
            }
        }
        return true;
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
        return new Note(Note.A, Note.QUARTER_NOTE, 5, Note.SHARP, 127, 60);
    }
}
