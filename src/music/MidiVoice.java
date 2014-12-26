package music;

import java.util.List;

public class MidiVoice implements Voice {
    private final List<SoundElement> sequence;
    private final List<Integer> timesUntilNextElement;
    private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private SoundElement currentElement;
    
    public MidiVoice(List<SoundElement> sequence, List<Integer> timesUntilNextElement, Instrument instrument) {
        this.sequence = sequence;
        this.timesUntilNextElement = timesUntilNextElement;
        this.instrument = instrument;
    }
    
    public SoundElement next() {
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
    
    public int duration() {
        int duration = 0;
        for ( int timeUntilNextElement : timesUntilNextElement ) {
            duration += timeUntilNextElement;
        }
        duration += sequence.get( sequence.size() - 1 ).duration();
        return duration;
    }
}
