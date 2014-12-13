package music;

import java.io.File;
import java.io.FileNotFoundException;

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
        return Parser.fileToMusic( musicFile );
    }
}
