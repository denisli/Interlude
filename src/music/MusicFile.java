package music;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.midi.MidiUnavailableException;

import music.parser.MidiParser;

public class MusicFile {
    private final String musicTitle;
    private final String musicFileName;
    
    public MusicFile( String title, String fileName ) {
        this.musicTitle = title;
        this.musicFileName = fileName;
    }

    public String musicTitle() {
        return musicTitle;
    }
    
    public String fileName() {
        return musicFileName;
    }
    
    public Music getMusic() throws FileNotFoundException {
        //return Parser.fileToMusic( musicFile );
        try {
            return MidiParser.parse( musicTitle, musicFileName );
        } catch (MidiUnavailableException mue) {
            // TODO: do something else with this exception;
            throw new RuntimeException("Midi not available");
        }
    }
}
