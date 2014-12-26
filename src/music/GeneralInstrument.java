package music;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import util.Triple;

public class GeneralInstrument implements Instrument {
    
    private String instrumentName;
    private OffTimesComparator comparator = new OffTimesComparator();
    private PriorityQueue<Triple<Long,Integer,Integer>> offTimes = new PriorityQueue<Triple<Long,Integer,Integer>>(comparator);
    private MidiChannel currentPlayer;
    private MidiChannel[] channels;
    private int[] occupiedChannels;
    private int idx = 0;
    private long currentTime = 0;
    private final int programNumber;
    
    public GeneralInstrument( int programNumber, int... occupiedChannels ) {
        Synthesizer synth = LoadSynthesizer.getSynthesizer();
        javax.sound.midi.Instrument[] instruments = synth.getDefaultSoundbank().getInstruments();
        this.programNumber = programNumber;
        this.instrumentName = instruments[programNumber].getName();
        MidiChannel[] channels = synth.getChannels();
        
        for ( int i : occupiedChannels ) {
            channels[i].programChange( instruments[programNumber].getPatch().getProgram() );
        }
        
        System.out.println(instruments[programNumber].getName());
        this.channels = channels;
        this.currentPlayer = channels[occupiedChannels[0]];
        this.occupiedChannels = occupiedChannels;
    }
    
    @Override
    public void update(int t) {
        currentTime += t;
        while ( !offTimes.isEmpty() ) {
            if ( currentTime >= offTimes.peek().getLeft() ) {
                Triple<Long,Integer,Integer> pair = offTimes.poll();
                int channelIdx = pair.getMiddle();
                int pitch = pair.getRight();
                channels[channelIdx].noteOff(pitch);
            } else {
                break;
            }
        }
    }
    
    @Override
    public void play(Note note) {
        int channelIdx = occupiedChannels[idx];
        currentPlayer = channels[ channelIdx ];
        int pitch = note.pitch();
        int volume = note.volume();
        int duration = note.duration();
        System.out.println("Note tick: " + note.tick() + ", Program Number: " + currentPlayer.getProgram() + ", Index: " + idx + ", Pitch: " + pitch + ", Volume: " + volume + ", Duration: " + duration);
        currentPlayer.noteOn( pitch, volume );
        offTimes.add( new Triple<Long,Integer,Integer>( currentTime + duration, channelIdx, pitch ) );
        idx = ( idx + 1 ) % occupiedChannels.length;
    }
    
    @Override
    public void play(Simultaneous simultaneous) {
        for ( SoundElement musicElement : simultaneous.soundElements() ) {
            musicElement.bePlayed( this );
        }
    }
    
    @Override
    public InstrumentType type() {
        //if ( TWO_HANDED_INSTRUMENTS.contains( currentPlayer.getProgram() ) ) {
        //    return InstrumentType.DOUBLE;
        //} else {
            return InstrumentType.SINGLE;
        //}
    }

    @Override
    public String getInstrumentName() {
        // TODO Auto-generated method stub
        return instrumentName;
    }
    
    @Override
    public boolean equals(Object other) {
        if ( ! (other instanceof Instrument) ) {
            return false;
        } else {
            Instrument otherInstrument = (Instrument) other;
            return this.instrumentName.equals(otherInstrument.getInstrumentName());
        }
    }

    @Override
    public int getProgram() {
        // TODO Auto-generated method stub
        return programNumber;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        while ( !offTimes.isEmpty() ) {
            Triple<Long,Integer,Integer> triple = offTimes.remove();
            int channelIdx = triple.getMiddle();
            int pitch = triple.getRight();
            channels[channelIdx].noteOff(pitch);
        }
    }
}
