package music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;



public class Piano implements Instrument {
    private MidiChannel piano;
    private MidiChannel otherPiano;
    private MidiChannel currentPlayer;
    public Piano() {
        Synthesizer synth;
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            piano = synth.getChannels()[0]; // 0 is the MidiChannel representing a piano
            //Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
            //channels[0].programChange(instruments[0].getPatch().getProgram());
            otherPiano = synth.getChannels()[1];
            currentPlayer = piano;
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void play(Rest rest) {
        return;
    }
    
    @Override
    public void play(Note note) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if ( currentPlayer == piano ) {
                        currentPlayer = otherPiano;
                    } else {
                        currentPlayer = piano;
                    }
                    int pitch = note.pitch();
                    int volume = note.volume();
                    int duration = note.duration();
                    currentPlayer.noteOn( pitch, volume );
                    Thread.sleep( duration );
                    currentPlayer.noteOff( pitch );
                    
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }).start();
    }
    
    @Override
    public void play(Simultaneous simultaneous) {
        new Thread(new Runnable() {
           @Override
           public void run() {
               for (MusicElement element : simultaneous.musicElements()) {
                   if ( element.isRest() ) {
                       play( (Rest) element );
                   } else {
                       play( (Note) element );
                   }
               }
           }
        }).start();
    }
    
    @Override
    public String getInstrumentName() {
        return this.getClass().getSimpleName();
    }
}
