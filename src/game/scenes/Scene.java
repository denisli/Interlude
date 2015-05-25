package game.scenes;

import java.util.ArrayList;
import java.util.List;

import music.Instrument;
import music.Music;
import game.Renderable;
import game.Updateable;
import game.pop_ups.PopUp;
import game.scenes.change_controls.ChangeControls;
import game.scenes.initialization_scene.InitializationScene;
import game.scenes.instructions.Instructions;
import game.scenes.instrument_selection.InstrumentSelectionPage;
import game.scenes.main_menu.MainMenu;
import game.scenes.options.OptionsPage;
import game.scenes.results_page.ResultsPage;
import game.scenes.round.Round;
import game.scenes.song_selection.SongSelectionPage;

import org.newdawn.slick.Graphics;

public abstract class Scene implements Renderable, Updateable {
    protected final List<PopUp> popUps = new ArrayList<PopUp>();
    protected final List<PopUp> popUpsToRemove = new ArrayList<PopUp>();
    
    public static Scene initializationScene() {
        Scene scene = new InitializationScene();
        scene.init();
        return scene;
    }
    
    public static Scene mainMenu() {
        Scene scene = new MainMenu();
        scene.init();
        return scene;
    }
    
    public static Scene songSelection() {
        Scene scene = new SongSelectionPage();
        scene.init();
        return scene;
    }
    
    public static Scene instructionsPage() {
        Scene scene = new Instructions();
        scene.init();
        return scene;
    }
    
    public static Scene optionsPage() {
        Scene scene = new OptionsPage();
        scene.init();
        return scene;
    }
    
    public static Scene instrumentSelection( Music music ) {
        Scene scene = new InstrumentSelectionPage( music );
        scene.init();
        return scene;
    }
    
    public static Scene round(Music music, Instrument selectedInstrument) {
        Scene scene = new Round(music, selectedInstrument);
        scene.init();
        return scene;
    }
    
    public static Scene results(String musicTitle, int score) {
        Scene scene = new ResultsPage( musicTitle, score );
        scene.init();
        return scene;
    }
    
    public static Scene changeControlsPage() {
        Scene scene = new ChangeControls();
        scene.init();
        return scene;
    }
    
    /**
     * Adds an initialized pop up to the scene
     * @param popUp
     */
    public void addPopUp(PopUp popUp) {
        popUps.add(popUp);
    }
    
    public void destroyPopUp(PopUp popUp) {
        popUpsToRemove.add(popUp);
    }
    
    public abstract Scene parentScene();
    
    public void render(Graphics g) {
        popUps.stream().forEach( popUp -> popUp.render(g) );
    }
    
    public void update(int t) {
        popUps.stream().forEach( popUp -> popUp.update(t) );
        popUpsToRemove.stream().forEach( popUp -> popUps.remove(popUp) );
    }
    
    public void init() {
        layout();
    }
    
    protected abstract void layout();
    
    protected abstract void cleanUp();
    
    protected abstract void handleServerMessages();
}
