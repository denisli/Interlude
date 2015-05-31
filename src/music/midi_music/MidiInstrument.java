package music.midi_music;

import java.util.PriorityQueue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

import util.Quadruple;
import music.Instrument;
import music.InstrumentType;
import music.LoadSynthesizer;
import music.Note;
import music.OffTimesComparator;

public class MidiInstrument implements Instrument {
	private boolean pause;
	private double volumeRatio = 1.0;
	private MidiChannel[] channels;
	private int playingChannel;
	private int programNumber;
	private String instrumentName;
	private final OffTimesComparator comparator = new OffTimesComparator();
	private final PriorityQueue<Quadruple<Long,Integer,Integer,Integer>> offTimes = new PriorityQueue<Quadruple<Long,Integer,Integer,Integer>>(comparator);
	private long currentTime;
	
	public MidiInstrument(int programNumber, int... occupiedChannelNumbers) {
		Synthesizer synth = LoadSynthesizer.getSynthesizer();
		channels = synth.getChannels();
		instrumentName = synth.getDefaultSoundbank().getInstruments()[programNumber].getName();
		for ( int channel : occupiedChannelNumbers ) {
			channels[channel].programChange(programNumber);
		}
	}
	
	@Override
    public void update(int t) {
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
    }
    
    @Override
    public void play(Note note) {
        int pitch = note.pitch();
        int volume = note.volume();
        int duration = note.duration();
        MidiChannel channel = channels[playingChannel];
        System.out.println("Tick: " + note.tick() + ", Channel: " + playingChannel + ", Program Number: " + channel.getProgram() + ", Pitch: " + pitch + ", Volume: " + volume + ", Duration: " + duration);
        offTimes.add( new Quadruple<Long,Integer,Integer,Integer>( currentTime + duration, playingChannel, pitch, volume ) );
        channel.noteOn( pitch, volume );
    }
    
    @Override
    public void pause() {
    	pause = true;
    	clear();
    }
    
    @Override
    public void resume() {
    	pause = false;
    }

	@Override
	public String getInstrumentName() {
		return instrumentName;
	}

	public void setPlayingChannel(int channelNumber) {
		playingChannel = channelNumber;
	}
	
	public void setControl(int controller, int value, int channelNumber) {
		MidiChannel channel = channels[channelNumber];
		channel.controlChange(controller, value);
	}

	@Override
	public boolean equals(Object other) {
		if ( !(other instanceof MidiInstrument) ) {
			return false;
		} else {
			MidiInstrument otherInstrument = (MidiInstrument) other;
			return otherInstrument.instrumentName == this.instrumentName;
		}
	}
	
	@Override
	public int hashCode() {
		return instrumentName.hashCode();
	}

	@Override
	public InstrumentType type() {
		if ( programNumber == 0 ) {
            return InstrumentType.DOUBLE;
        } else {
            return InstrumentType.SINGLE;
        }
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
