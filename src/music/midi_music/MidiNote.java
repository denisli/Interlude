package music.midi_music;

import music.Note;

public class MidiNote extends Note {
	private final int channelNumber;
	
	public MidiNote(int pitch, int duration, int volume, int channelNumber) {
		super(pitch, duration, volume);
		this.channelNumber = channelNumber;
	}
	
	public int pitch() {
		return super.pitch();
	}
	
	public int duration() {
		return super.duration();
	}
	
	public int volume() {
		return super.volume();
	}
	
	public int getChannelNumber() {
		return channelNumber;
	}
}
