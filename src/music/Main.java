package music;

import java.util.Arrays;
import java.util.List;

public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("YO");
        Instrument piano = Instrument.piano();
        Note note = new Note( 88+12, 1000, 127 );
        
        List<SoundElement> musicElements = Arrays.asList(note);
        Simultaneous simultaneous = new Simultaneous ( musicElements );
        simultaneous.bePlayed(piano);
        Thread.sleep(500);
        simultaneous.bePlayed(piano);
        Thread.sleep(500);
        simultaneous.bePlayed(piano);
        Thread.sleep(500);
        simultaneous.bePlayed(piano);
    }
}
