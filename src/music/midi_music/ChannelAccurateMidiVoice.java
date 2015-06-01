package music.midi_music;

import java.util.List;

import music.Instrument;
import music.Note;
import music.Note;
import music.Voice;

public class ChannelAccurateMidiVoice implements Voice {
	
	private final List<MidiNote> notes;
	private final List<Integer> timesUntilNextNote;
	
	private final Instrument instrument;
    
    private int index = -1; // index of the music element currently playing
    private Note currentElement;
    
    public ChannelAccurateMidiVoice(List<MidiNote> notes, List<Integer> timesUntilNextNote, Instrument instrument) {
        this.notes = notes;
        this.timesUntilNextNote = timesUntilNextNote;
        this.instrument = instrument;
    }
    
    public Note nextNote() {
        currentElement = notes.get(++index);
        return currentElement;
    }
    
    public int timeUntilNextNote() {
        return timesUntilNextNote.get(index + 1);
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return index >= (notes.size() - 1);
    }
    
    @Override
    public void restart() {
        index = -1;
    }
    
    public int duration() {
        int duration = 0;
        for ( int timeUntilNextNote : timesUntilNextNote ) {
            duration += timeUntilNextNote;
        }
        duration += notes.get( notes.size() - 1 ).duration();
        return duration;
    }
}
