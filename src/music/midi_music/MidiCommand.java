package music.midi_music;

public class MidiCommand {
	public enum Type {
		CONTROLLER, PITCH_BEND;
	}
	
	Type type;
	int[] arguments;
	
	public MidiCommand(Type type, int... arguments) {
		this.type = type;
		this.arguments = arguments;
	}
	
	public void applyCommand(MidiInstrument instrument) {
		// fix this stuff
		switch (type) {
		case CONTROLLER:
			int controller = arguments[0];
			int value = arguments[1];
			int channelNumber = arguments[2];
			instrument.setControl(controller, value, channelNumber);
		case PITCH_BEND: break;
		default: break;
		}
	}
}
