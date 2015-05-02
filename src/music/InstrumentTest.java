package music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class InstrumentTest implements Instrument {
	MidiChannel[] channels;
	int index = 0;
	public InstrumentTest(int programNumber) throws MidiUnavailableException {
		Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        MidiChannel[] channels = synth.getChannels();
        for ( MidiChannel channel : channels ) {
        	channel.programChange( instruments[programNumber].getPatch().getProgram() );
        }
        this.channels = channels;
	}

	@Override
	public void update(int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play(Note note) {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int pitch = note.pitch();
				int duration = note.duration();
				int volume = note.volume();
				int idx = 9;//index;
				index++;
				channels[idx].noteOn(pitch, volume);
				try {
					System.out.println(Thread.currentThread());
					Thread.sleep(duration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				channels[idx].noteOff(pitch);
			}
		});
		thread.start();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getInstrumentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getProgram() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InstrumentType type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
}
