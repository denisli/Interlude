package music.midi_music;

import java.util.List;

import music.Instrument;
import music.Note;
import music.Voice;

public class SuperAccurateMidiVoice implements Voice {
	
	private final List<MidiNote> notes;
	private final List<Integer> timesUntilNextNote;
	private final List<MidiControlChange> commands;
	private final List<Integer> timesUntilNextCommand;
	
	private final Instrument instrument;
    
    private int noteIndex = -1; // index of the music element currently playing
    private int commandIndex = -1; // index of the current command
    
    public SuperAccurateMidiVoice(List<MidiNote> notes, List<Integer> timesUntilNextNote, List<MidiControlChange> commands, List<Integer> timesUntilNextCommand, SuperMidiInstrument instrument) {
        this.notes = notes;
        this.timesUntilNextNote = timesUntilNextNote;
        this.commands = commands;
        this.timesUntilNextCommand = timesUntilNextCommand;
        this.instrument = instrument;
    }
    
    public Note nextNote() {
        return notes.get(++noteIndex);
    }
    
    public int timeUntilNextNote() {
        return timesUntilNextNote.get(noteIndex + 1);
    }
    
    public MidiControlChange nextCommand() {
    	return commands.get(++commandIndex);
    }
    
    public int timeUntilNextCommand() {
    	return timesUntilNextCommand.get(commandIndex + 1);
    }
    
    public Instrument instrument() {
        return instrument;
    }
    
    public boolean ended() {
        return noteIndex >= (notes.size() - 1);
    }
    
    public boolean noMoreControlChanges() {
    	return commandIndex >= (commands.size() - 1);
    }
    
    @Override
    public void restart() {
        noteIndex = -1;
        commandIndex = -1;
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
