package music.parser;

import java.io.InputStream;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiParser {
    
    /**
     * Found here: http://stackoverflow.com/questions/3850688/reading-midi-files-in-java
     * Written by Sami Koivu
     * @throws Exception
     */
    public static void analyze() throws Exception {
        final String[] NOTE_NAMES = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
        final int NOTE_ON = 0x90;
        final int NOTE_OFF = 0x80;
        //InputStream in = ClassLoader.getSystemResourceAsStream("midi/26799_What-Ive-Done.mid");
        InputStream in = ClassLoader.getSystemResourceAsStream("midi/secret base ~Kimi ga Kureta Mono~.mid");
        int x = 2;
        if ( x == 1 ) return;
        Sequence sequence = MidiSystem.getSequence(in);
        System.out.println("Division Type: " + sequence.getDivisionType());
        int trackNumber = 0;
        for (Track track :  sequence.getTracks()) {
            trackNumber++;
            System.out.println("Track " + trackNumber + ": size = " + track.size());
            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
                if ( event.getTick() > 20000 ) { continue; }
                System.out.print("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    System.out.print("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if (sm.getCommand() == NOTE_OFF) {
                        int key = sm.getData1();
                        int octave = (key / 12)-1;
                        int note = key % 12;
                        String noteName = NOTE_NAMES[note];
                        int velocity = sm.getData2();
                        System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                    } else if ( sm.getCommand() == ShortMessage.PROGRAM_CHANGE ) {
                        System.out.println("Program Change, " + sm.getData1());
                    } else {
                        System.out.println("Command: " + sm.getCommand());
                        if ( sm.getCommand() == ShortMessage.CONTROL_CHANGE ) {
                        	System.out.println("\tController: " + sm.getData1() + ", Value: " + sm.getData2());
                        }
                    }
                } else if ( message instanceof MetaMessage ) {
                    MetaMessage mm = (MetaMessage) message;
                    System.out.println("Type of MetaMessage: " + mm.getType());
                } else {
                    System.out.println("Here");
                }
            }

            System.out.println();
        }
    }
    
    public static void main(String[] args) throws Exception {
        analyze();
    }
}
