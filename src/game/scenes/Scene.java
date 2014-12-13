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
    
    public static Scene instructionsPage() {
        return new Instructions();
    }
    
    public static Scene round(Music music ) {
        return new Round(music);
    }
    
    public void fireActivatedButtons();
    
    public Scene parentScene();
    
    public String name();
    
    public void render(Graphics g);
    
    public void update(int t);
    
    public void init();
}
