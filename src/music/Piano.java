package music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;



public class Piano implements Instrument {
    private MidiChannel piano;
    public Piano() {
        Synthesizer synth;
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            piano = synth.getChannels()[0]; // 0 is the MidiChannel representing a piano
        } catch (MidiUnavailableException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public void playSoundElement(SoundElement soundElement) {
        if ( soundElement instanceof Note ) {
            new Thread(new Runnable() {
                public void run() {
                    if ( soundElement instanceof Note ) {
                        Note note = (Note) soundElement;
                        try {
                            int pitch = note.pitch(); int duration = note.duration(); int volume = note.volume();
                            piano.noteOn( pitch, volume );
                            Thread.sleep( duration );
                            piano.noteOff( pitch );
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                }
            }).start();
        } else if (soundElement instanceof MusicElement) {
            Simultaneous simultaneous = (Simultaneous) soundElement;
            for (Note note : simultaneous.notes()) {
                Piano.this.playSoundElement(note);
            }
        }
    }
    
    @Override
    public String getInstrumentName() {
        return this.getClass().getSimpleName();
    }
}
