package game.scenes;

import music.Music;
import game.Controls;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface Scene {
    public static Scene mainMenu() {
        return new MainMenu();
    }
    
    public static Scene songSelection() {
        return new SongSelectionPage();
    }
    
    public static Scene round(Music music ) {
        if ( music.isMultiVoice() ) {
            return new TwoVoiceRound(music);
        } else {
            return new OneVoiceRound(music);
        }
    }
    
    public String name();
    
    public void render(GameContainer gc, Graphics g);
    
    public void update(GameContainer gc, int t);
    
    public void init(GameContainer gc);
    
    public Scene nextScene(GameContainer gc, int t);
}
