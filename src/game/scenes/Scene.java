package game.scenes;

import music.Instrument;


public interface Scene {
    public static Scene mainMain() {
        return new MainMenu();
    }
    public static Scene songSelection(String songTitle, Instrument instrument ) {
        return new SongSelection( songTitle, instrument );
    }
}
