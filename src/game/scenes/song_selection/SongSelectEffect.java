package game.scenes.song_selection;

import game.scenes.Scene;
import game.scenes.SceneManager;
import music.Instrument;
import music.Music;
import music.MusicFile;
import music.parser.MidiParserImproved;
import music.parser.MidiParserPlus;
import music.parser.MidiParserSuper;

public class SongSelectEffect implements Runnable {
    private final MusicFile musicFile;
    
    public SongSelectEffect( MusicFile musicFile ) {
        this.musicFile = musicFile;
    }

    @Override
    public void run() {
        try {
            Music music = MidiParserPlus.parse( musicFile.musicTitle(), musicFile.fileName() );
            if ( music.isMultiInstrument() ) {
                SceneManager.setNewScene( Scene.instrumentSelection( music ) );
            } else {
                Instrument selectedInstrument = music.voices().get(0).instrument();
                SceneManager.setNewScene( Scene.round(music, selectedInstrument) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
