package game.scenes.song_selection;

import game.scenes.Scene;
import game.scenes.SceneManager;

import javax.sound.midi.MidiUnavailableException;

import music.Instrument;
import music.Music;
import music.MusicFile;
import music.parser.MidiParser;

public class SongSelectEffect implements Runnable {
    private final MusicFile musicFile;
    
    public SongSelectEffect( MusicFile musicFile ) {
        this.musicFile = musicFile;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Music music = MidiParser.parse( musicFile.musicTitle(), musicFile.fileName() );
            if ( music.isMultiInstrument() ) {
                SceneManager.setNewScene( Scene.instrumentSelection( music ) );
            } else {
                Instrument selectedInstrument = music.voices().get(0).instrument();
                SceneManager.setNewScene( Scene.round(music, selectedInstrument) );
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}
