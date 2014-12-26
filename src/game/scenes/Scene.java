package game.scenes;

import java.util.ArrayList;
import java.util.List;

import music.Instrument;
import music.Music;
import game.pop_ups.PopUp;

import org.newdawn.slick.Graphics;

public abstract class Scene {
    protected final List<PopUp> popUps = new ArrayList<PopUp>();
    
    public static Scene initializationScene() {
        Scene scene = new InitializationScene();
        //scene.init();
        return scene;
    }
    
    public static Scene mainMenu() {
        return new MainMenu();
    }
    
    public static Scene connectWithFriends() {
        return new ConnectWithFriends();
    }
    
    public static Scene songSelection() {
        return new SongSelectionPage();
    }
    
    public static Scene instructionsPage() {
        return new Instructions();
    }
    
    public static Scene instrumentSelection( Music music ) {
        return new InstrumentSelectionPage( music );
    }
    
    public static Scene round(Music music, Instrument selectedInstrument) {
        return new Round(music, selectedInstrument);
    }
    
    public static Scene results(String musicTitle, int score) {
        return new ScorePage( musicTitle, score );
    }
    
    public static Scene changeControlsPage() {
        return new ChangeControls();
    }
    
    public abstract void addPopUp(PopUp popUp);
    
    public abstract void destroyPopUp(PopUp popUp);
    
    public abstract Scene parentScene();
    
    public abstract void render(Graphics g);
    
    public abstract void update(int t);
    
    public abstract void init();
    
    public abstract void cleanUp();
}
