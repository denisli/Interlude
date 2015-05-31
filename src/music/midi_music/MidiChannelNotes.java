package music.midi_music;

import java.util.ArrayList;
import java.util.List;

public class MidiChannelNotes {
	private final int channelNumber;
	private final List<MidiNote> notes = new ArrayList<MidiNote>();
	private final List<Integer> timesUntilNextNote = new ArrayList<Integer>();
	
	private int index;
	
	public MidiChannelNotes(int channelNumber) {
		this.channelNumber = channelNumber;
	}
	
	public int getChannelNumber() {
		return channelNumber;
	}
	
	public void add(MidiNote midiNote, int timeUntilNote) {
		this.notes.add(midiNote);
		this.timesUntilNextNote.add(timeUntilNote);
	}
	
	public void next() {
		index++;
	}
	
	public MidiNote nextNote() {
		return notes.get(index);
	}
	
	public int timeUntilNextNote() {
		return timesUntilNextNote.get(index);
	}
	
}
