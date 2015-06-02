package music.parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.MidiEvent;

public class MidiControlEvents implements Iterable<MidiEvent> {
private List<MidiEvent> events = new ArrayList<MidiEvent>();
	
	public void add(MidiEvent event) {
		events.add(event);
	}
	
	public MidiEvent get(int index) {
		return events.get(index);
	}
	
	public void sort() {
		events.sort(new Comparator<MidiEvent>() {
			@Override
			public int compare(MidiEvent event1, MidiEvent event2) {
				long tick1 = event1.getTick();
				long tick2 = event2.getTick();
				return (int) (tick1 - tick2);
			}
		});
	}
	
	@Override
	public Iterator<MidiEvent> iterator() {
		return events.iterator();
	}
	
	public List<MidiEvent> asList() {
		return events;
	}
}
