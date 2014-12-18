package music;


import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class SuperPlayer implements Instrument {
    private MidiChannel[] channels;
    
    public SuperPlayer() throws MidiUnavailableException {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        this.channels = synth.getChannels();
    }

    @Override
    public void play(Rest rest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void play(Note note) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            public void run() {
                
            }
        })
    }

    @Override
    public void play(Simultaneous simultaneous) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getInstrumentName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
