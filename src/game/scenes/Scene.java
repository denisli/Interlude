package game.scenes;

import game.Controls;
import game.Game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import music.Instrument;


public interface Scene {
    public static Scene mainMenu() {
        return new MainMenu();
    }
    
    public static Scene songSelection() {
        return new SongSelectionPage();
    }
    
    public static Scene round(String name, String songTitle, Instrument instrument, Controls controls ) {
        return new Round( name, songTitle, instrument, controls );
    }
    
    public String name();
    
    public void render(GameContainer gc, Graphics g);
    
    public void update(GameContainer gc, int t);
    
    public void init(GameContainer gc);
    
    public Scene nextScene(GameContainer gc, int t);
}
