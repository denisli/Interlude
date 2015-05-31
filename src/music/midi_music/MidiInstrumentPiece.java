package music.midi_music;

import java.util.List;

import music.Instrument;
import music.InstrumentPiece;
import music.Note;

public class MidiInstrumentPiece implements InstrumentPiece {
	private final List<MidiChannelNotes> midiChannelsOfNotes;
	private final List<MidiChannelCommands> midiChannelsOfCommands;
	private final Instrument instrument;

	private int noteIndex = 0;
	private int commandIndex = 0;
	
	public MidiInstrumentPiece(List<MidiChannelNotes> midiChannelsOfNotes, List<MidiChannelCommands> midiChannelsOfCommands, List<MidiCommand> commands, List<Integer> timesUntilCommands, Instrument instrument) {
		this.midiChannelsOfNotes = midiChannelsOfNotes;
		this.midiChannelsOfCommands = midiChannelsOfCommands;
		this.instrument = instrument;
	}
	
	@Override
	public void updateMidiChannelsOfNotes() {
		midiChannelsOfNotes.
	}
	
	public MidiCommand nextCommand() {
		return commands.get(commandIndex);
	}
	
	public int timeUntilNextCommand() {
		return timesUntilCommands.get(commandIndex);
	}

	@Override
	public MidiNote nextNote() {
		return notes.get(noteIndex);
	}

	@Override
	public int timeUntilNextNote() {
		return timesUntilNextNote.get(noteIndex);
	}

	@Override
	public int duration() {
		int duration = 0;
		for ( int time : timesUntilNextNote ) {
			duration += time;
		}
		duration += notes.get(notes.size()-1).duration();
		return duration;
	}

	@Override
	public Instrument instrument() {
		return instrument;
	}

	@Override
	public void restart() {
		noteIndex = 0;
	}
}
