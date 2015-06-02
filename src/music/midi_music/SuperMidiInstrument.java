package music.midi_music;

import java.util.PriorityQueue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import util.Quadruple;
import music.Instrument;
import music.LoadSynthesizer;
import music.Note;
import music.OffTimesComparator;

public class SuperMidiInstrument implements Instrument {
	private boolean paused;
	private double volumeRatio = 1.0;
	private final MidiChannel[] channels;
	private final String instrumentName;
	private final OffTimesComparator comparator = new OffTimesComparator();
	private final PriorityQueue<Quadruple<Long,Integer,Integer,Integer>> offTimes = new PriorityQueue<Quadruple<Long,Integer,Integer,Integer>>(comparator);
	private SuperAccurateMidiVoice voice;
	
	private long currentTime = 0;
	private long restingTime = 0;
	
	public SuperMidiInstrument(int programNumber, int... occupiedChannelNumbers) {
		Synthesizer synth = LoadSynthesizer.getSynthesizer();
		channels = synth.getChannels();
		javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
		this.instrumentName = (occupiedChannelNumbers[0] == 9) ? "Percussion" : instruments[programNumber].getName();
		for ( int channel : occupiedChannelNumbers ) {
			channels[channel].programChange(programNumber);
		}
	}
	
	public void teachInstrument(SuperAccurateMidiVoice voice) {
		this.voice = voice;
		if ( !voice.noMoreControlChanges() ) {
			restingTime = voice.timeUntilNextCommand();
		}
	}
	
	@Override
    public void update(int t) {
		if ( !paused ) {
			// determine when to turn off notes
			currentTime += t;
			while ( !offTimes.isEmpty() ) {
				if ( currentTime >= offTimes.peek().getLeft() ) {
					Quadruple<Long,Integer,Integer,Integer> quad = offTimes.poll();
					int channelIdx = quad.getMiddleLeft();
					int pitch = quad.getMiddleRight();
					channels[channelIdx].noteOff(pitch);
				} else {
					break;
				}
			}
			// determine when to do a new control change
			restingTime -= t;
			if ( restingTime <= 0 ) {
				do {
					if ( !voice.noMoreControlChanges() ) {
						MidiControlChange controlChange = voice.nextCommand();
						controlChange.applyControlChange(this);
					} else {
						break;
					}
					if ( !voice.noMoreControlChanges() ) {
						restingTime += voice.timeUntilNextCommand();
					} else {
						break;
					}
				} while ( restingTime <= 0 );
			}
		}
    }
    
    @Override
    public void play(Note note) {
    	MidiNote midiNote = (MidiNote) note;
    	int channelNumber = midiNote.getChannelNumber();
        int pitch = note.pitch();
        int volume = note.volume();
        int duration = note.duration();
        MidiChannel channel = channels[channelNumber];
        System.out.println("Tick: " + note.tick() + ", Channel: " + channelNumber + ", Program Number: " + channel.getProgram() + ", Pitch: " + pitch + ", Volume: " + volume + ", Duration: " + duration);
        offTimes.add( new Quadruple<Long,Integer,Integer,Integer>( currentTime + duration, channelNumber, pitch, volume ) );
        channel.noteOn( pitch, volume );
    }
    
    @Override
    public void pause() {
    	paused = true;
    	clear();
    }
    
    @Override
    public void resume() {
    	paused = false;
    }

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	@Override
	public boolean equals(Object other) {
		if ( !(other instanceof SuperMidiInstrument) ) {
			return false;
		} else {
			SuperMidiInstrument otherInstrument = (SuperMidiInstrument) other;
			return otherInstrument.instrumentName == this.instrumentName;
		}
	}
	
	public void applyControlChange(int channelNumber, int controller, int value) {
		channels[channelNumber].controlChange(controller, value);
	}
	
	@Override
	public int hashCode() {
		return instrumentName.hashCode();
	}

	@Override
	public void setVolumeRatio(double volumeRatio) {
		this.volumeRatio = volumeRatio;
	}

	@Override
	public void clear() {
		while ( !offTimes.isEmpty() ) {
    		Quadruple<Long,Integer,Integer,Integer> quad = offTimes.remove();
    		int channelIdx = quad.getMiddleLeft();
    		int pitch = quad.getMiddleRight();
    		channels[channelIdx].noteOff(pitch);
    	}
	}
}
