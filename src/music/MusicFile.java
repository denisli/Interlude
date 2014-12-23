package music;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.midi.MidiUnavailableException;

import music.parser.MidiParser;

public class MusicFile {
    private final String musicTitle;
    private final File musicFile;
    
    public MusicFile( String title, File file ) {
        this.musicTitle = title;
        this.musicFile = file;
    }

    public String musicTitle() {
        return musicTitle;
    }
    
    public Music getMusic() throws FileNotFoundException {
        //return Parser.fileToMusic( musicFile );
        try {
            return MidiParser.parse( musicFile, 0 );
        } catch (MidiUnavailableException mue) {
            // TODO: do something else with this exception;
            throw new RuntimeException("Midi not available");
        }
    }
}
