package music;

import java.util.List;

public class TextVoice implements Voice {
    private final List<SoundElement> sequence;
    private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private SoundElement currentElement;
    
    public TextVoice(List<SoundElement> sequence, Instrument instrument) {
        this.sequence = sequence;
        this.instrument = instrument;
    }
    
    public SoundElement next() {
        index += 1;
        currentElement = sequence.get(index);
        return currentElement;
    }
    
    public int timeUntilNextElement() {
        throw new RuntimeException("Not implemented yet");
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return index == sequence.size() - 1;
    }
    
    public int duration() {
        int duration = 0;
        for ( SoundElement soundElement: sequence ) {
            duration += soundElement.duration();
        }
        return duration;
    }
}
