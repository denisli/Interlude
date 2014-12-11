package music;

import java.util.Arrays;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("YO");
        Instrument piano = Instrument.piano();
        Note note = new Note( Note.G, Note.QUARTER_NOTE, 6, Note.SHARP, 127, 60 );
        List<MusicElement> musicElements = Arrays.asList(note);
        Simultaneous simultaneous = new Simultaneous ( musicElements );
        simultaneous.bePlayed(piano);
    }
}
