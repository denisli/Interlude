package game.scenes;

import music.Music;
import game.pop_ups.PopUp;

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
    
    public static Scene changeControlsPage() {
        return new ChangeControls();
    }
    
    public void addPopUp(PopUp popUp);
    
    public void destroyPopUp(PopUp popUp);
    
    public Scene parentScene();
    
    public String name();
    
    public void render(Graphics g);
    
    public void update(int t);
    
    public void init();
}
