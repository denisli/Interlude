package music;

import java.util.PriorityQueue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import util.Quadruple;

public class GeneralInstrument implements Instrument {
	private double volumeRatio = 1.0;
	private String instrumentName;
	private final OffTimesComparator comparator = new OffTimesComparator();
	/** triple = ( time to turn off, channel idx, pitch ) */
	private final PriorityQueue<Quadruple<Long, Integer, Integer, Integer>> offTimes = new PriorityQueue<Quadruple<Long, Integer, Integer, Integer>>(
			comparator);
	private MidiChannel currentPlayer;
	private final MidiChannel[] channels;
	private final int[] occupiedChannels;
	private int idx = 0;
	private long currentTime = 0;
	private final int programNumber;

	private boolean paused = false;

	public GeneralInstrument(int programNumber, int... occupiedChannels) {
		Synthesizer synth = LoadSynthesizer.getSynthesizer();
		javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank()
				.getInstruments();
		this.programNumber = programNumber;
		this.instrumentName = (arrayContains(occupiedChannels, 9)) ? "Percussion"
				: instruments[programNumber].getName();
		MidiChannel[] channels = synth.getChannels();

		for (int i : occupiedChannels) {
			channels[i].programChange(instruments[programNumber].getPatch()
					.getProgram());
		}

		// System.out.println(instruments[programNumber].getName());
		this.channels = channels;
		this.currentPlayer = channels[occupiedChannels[0]];
		this.occupiedChannels = occupiedChannels;
	}

	private boolean arrayContains(int[] arr, int num) {
		for (int elem : arr) {
			if (elem == num)
				return true;
		}
		return false;
	}

	@Override
	public void update(int t) {
		if (!paused) {
			currentTime += t;
			while (!offTimes.isEmpty()) {
				if (currentTime >= offTimes.peek().getLeft()) {
					Quadruple<Long, Integer, Integer, Integer> pair = offTimes
							.poll();
					int channelIdx = pair.getMiddleLeft();
					int pitch = pair.getMiddleRight();
					channels[channelIdx].noteOff(pitch);
				} else {
					break;
				}
			}
		}
	}

	@Override
	public void play(Note note) {
		int channelIdx = occupiedChannels[idx];
		currentPlayer = channels[channelIdx];
		int pitch = note.pitch();
		int volume = (int) (Math.min(127, note.volume() * volumeRatio));
		int duration = note.duration();
		// System.out.println("Tick: " + note.tick() + ", Channel: " +
		// channelIdx + ", Program Number: " + currentPlayer.getProgram() +
		// ", Index: " + idx + ", Pitch: " + pitch + ", Volume: " + volume +
		// ", Duration: " + duration);
		offTimes.add(new Quadruple<Long, Integer, Integer, Integer>(currentTime
				+ duration, channelIdx, pitch, volume));
		currentPlayer.noteOn(pitch, volume);
		idx = (idx + 1) % occupiedChannels.length;
	}

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	@Override
	public void clear() {
		offTimes.clear();
		for (MidiChannel channel : channels) {
			channel.allSoundOff();
		}
	}

	@Override
	public void pause() {
		clear();
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Instrument)) {
			return false;
		} else {
			Instrument otherInstrument = (Instrument) other;
			return this.instrumentName.equals(otherInstrument
					.getInstrumentName());
		}
	}

	@Override
	public int hashCode() {
		return this.programNumber;
	}

	@Override
	public void setVolumeRatio(double volumeRatio) {
		this.volumeRatio = volumeRatio;
	}

	@Override
	public double getVolumeRatio() {
		return volumeRatio;
	}
}
