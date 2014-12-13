package music;

import game.VoiceType;

import java.util.List;

public class Voice {
    private final List<MusicElement> sequence;
    private final Instrument instrument;
    private final VoiceType voiceType;
    
    private int index = -1; // index of the music element currently playing
    private MusicElement currentElement;
    
    public Voice(List<MusicElement> sequence, Instrument instrument, VoiceType voiceType) {
        this.sequence = sequence;
        this.instrument = instrument;
        this.voiceType = voiceType;
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
    
    private VoiceType hand;
    
    public VoiceType getHand() {
        return hand;
    }
    
    public void setHand(VoiceType hand) {
        this.hand = hand;
    }
    
    public VoiceType voiceType() {
        return voiceType;
    }
}
