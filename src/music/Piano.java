package music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;



public class Piano implements Instrument {

    @Override
    public void playNote(Note note) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            MidiChannel piano = synth.getChannels()[0]; // 0 is the MidiChannel representing a piano
            int pitch = note.pitch(); int duration = note.duration(); int volume = note.volume();
            piano.noteOn( pitch, volume );
            Thread.sleep( duration );
            piano.noteOff( pitch );
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
