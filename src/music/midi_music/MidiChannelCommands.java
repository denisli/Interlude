package music.midi_music;

import java.util.ArrayList;
import java.util.List;

public class MidiChannelCommands {
	private final int channelNumber;
	private final List<MidiControlChange> commands = new ArrayList<MidiControlChange>();
	private final List<Integer> timesUntilNextCommand = new ArrayList<Integer>();
	
	private int index = 0;
	
	public MidiChannelCommands(int channelNumber) {
		this.channelNumber = channelNumber;
	}
	
	public int getChannelNumber() {
		return channelNumber;
	}
	
	public void next() {
		index++;
	}
	
	public void add(MidiControlChange command, int timeUntilCommand) {
		commands.add(command);
		timesUntilNextCommand.add(timeUntilCommand);
	}
	
	public MidiControlChange nextCommand() {
		return commands.get(index);
	}
	
	public int timeUntilNextCommand() {
		return timesUntilNextCommand.get(index);
	}
}
