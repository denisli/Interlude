package music.midi_music;

public class MidiControlChange {
	
	private final int channelNumber;
	private final int controller;
	private final int value;
	
	public MidiControlChange(int channelNumber, int controller, int value) {
		this.channelNumber = channelNumber;
		this.controller = controller;
		this.value = value;
	}
	
	public void applyControlChange(SuperMidiInstrument instrument) {
		instrument.applyControlChange(channelNumber, controller, value);
		System.out.println("Control change. Channel: " + channelNumber + ", " + "Controller: " + controller + ", Value: " + value);
	}
}
