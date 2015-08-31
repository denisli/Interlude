package music;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
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
    
    public Music getMusic() throws IOException {
        //return Parser.fileToMusic( musicFile );
        try {
            return MidiParser.parse( musicTitle, musicFileName );
        } catch (MidiUnavailableException mue) {
            throw new RuntimeException("Midi not available.");
        } catch (InvalidMidiDataException imde) {
			throw new RuntimeException("Midi data invalid.");
		}
    }
}
