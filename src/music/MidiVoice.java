package music;

import java.util.List;

public class MidiVoice implements Voice {
    private final List<Note> sequence;
    private final List<Integer> timesUntilNextElement;
    private final Handedness handedness;
    private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private Note currentElement;
    
    public MidiVoice(List<Note> sequence, List<Integer> timesUntilNextElement, Handedness handedness, Instrument instrument) {
        this.sequence = sequence;
        this.timesUntilNextElement = timesUntilNextElement;
        this.handedness = handedness;
        this.instrument = instrument;
    }
    
    public Note next() {
        index += 1;
        currentElement = sequence.get(index);
        return currentElement;
    }
    
    public int timeUntilNextElement() {
        return timesUntilNextElement.get(index);
    }
    
    public Handedness handedness() {
        return handedness;
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return index == sequence.size() - 1;
    }
    
    @Override
    public void restart() {
        index = -1;
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
