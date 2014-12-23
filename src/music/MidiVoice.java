package music;

import game.InstrumentType;

import java.util.List;

public class MidiVoice implements Voice {
    private final List<MusicElement> sequence;
    private final List<Integer> timesUntilNextElement;
    private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private MusicElement currentElement;
    
    public MidiVoice(List<MusicElement> sequence, List<Integer> timesUntilNextElement, Instrument instrument) {
        this.sequence = sequence;
        this.timesUntilNextElement = timesUntilNextElement;
        this.instrument = instrument;
    }
    
    public MusicElement next() {
        index += 1;
        currentElement = sequence.get(index);
        return currentElement;
    }
    
    public int timeUntilNextElement() {
        return timesUntilNextElement.get(index);
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return index == sequence.size() - 1;
    }
}
