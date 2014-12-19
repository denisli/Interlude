package music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class GeneralInstrument implements Instrument {
    MidiChannel generalInstrument;
    MidiChannel otherGeneralInstrument;
    MidiChannel currentPlayer;
    
    public GeneralInstrument( int programNumber ) {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
            MidiChannel[] channels = synth.getChannels();
            channels[0].programChange( instruments[programNumber].getPatch().getProgram() );
            channels[1].programChange( instruments[programNumber].getPatch().getProgram() );
            this.generalInstrument = channels[0];
            this.otherGeneralInstrument = channels[1];
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
    }

    @Override
    public void play(Rest rest) {
        // TODO Auto-generated method stub
        return;
    }

    @Override
    public void play(Note note) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if ( currentPlayer == generalInstrument ) {
                        currentPlayer = otherGeneralInstrument;
                    } else {
                        currentPlayer = generalInstrument;
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return null;
    }
}
