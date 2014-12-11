package music;

import java.util.List;

public class Voice {
    private final List<MusicElement> sequence;
    private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private MusicElement currentElement;
    
    public Voice(List<MusicElement> sequence, Instrument instrument) {
        this.sequence = sequence;
        this.instrument = instrument;
    }
    
    public MusicElement next() {
        index += 1;
        currentElement = sequence.get(index);
        return currentElement;
    }
    
    public int timeUntilNextElement() {
        return currentElement.timeUntilNextElement();
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return index == sequence.size() - 1;
    }
}
