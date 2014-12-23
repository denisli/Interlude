package music;

import game.InstrumentType;

import java.util.PriorityQueue;
import java.util.Queue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import util.Triple;



public class Piano implements Instrument {
    private Queue<Triple<Long,Integer,Integer>> offTimes = new PriorityQueue<Triple<Long,Integer,Integer>>(new OffTimesComparator());
    private MidiChannel[] pianos;
    private MidiChannel currentPlayer;
    private int idx = 0;
    private long currentTime = 0;
    
    public Piano() {
        try {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
            javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
            MidiChannel[] channels = synth.getChannels();
            channels[0].programChange( instruments[0].getPatch().getProgram() );
            channels[1].programChange( instruments[0].getPatch().getProgram() );
            pianos = channels;
            currentPlayer = pianos[0];
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        }
    }
    
    @Override
    public void update(int t) {
        currentTime += t;
        while ( !offTimes.isEmpty() ) {
            if ( currentTime >= offTimes.peek().getLeft() ) {
                Triple<Long,Integer,Integer> pair = offTimes.poll();
                int channelIdx = pair.getMiddle();
                int pitch = pair.getRight();
                pianos[channelIdx].noteOff(pitch);
            } else {
                break;
            }
        }
    }
    
    @Override
    public void play(Note note) {
        currentPlayer = pianos[idx];
        int pitch = note.pitch();
        int volume = note.volume();
        int duration = note.duration();
        currentPlayer.noteOn( pitch, volume );
        offTimes.add( new Triple<Long,Integer,Integer>( currentTime + duration, idx, pitch ) );
        idx = ( idx + 1 ) % 8;
    }
    
    @Override
    public void play(Simultaneous simultaneous) {
        for ( MusicElement musicElement : simultaneous.musicElements() ) {
            musicElement.bePlayed( this );
        }
    }
    
    @Override
    public void play(Rest rest) {
        return;
    }
    
    @Override
    public String getInstrumentName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public InstrumentType type() {
        // TODO Auto-generated method stub
        return InstrumentType.DOUBLE;
    }

    @Override
    public int getProgram() {
        // TODO Auto-generated method stub
        return 0;
    }
}
