package music;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class LoadSynthesizer {
    private static Synthesizer synth;
    
    public static void loadSynthesizer() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public static Synthesizer getSynthesizer() {
        return synth;
    }
}
