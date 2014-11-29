package game;

import game.buttons.SimpleFont;
import game.scenes.Scene;
import game.scenes.SceneManager;

import java.awt.Font;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import music.Instrument;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

public class Game extends BasicGame { 
    private GameContainer container;
    private final SceneManager sceneManager;
    private Controls controls;
    
    public Game() {
        super("Interlude");
        this.sceneManager = new SceneManager();
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        // TODO Auto-generated method stub
        sceneManager.render(gc, g);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        // TODO Auto-generated method stub
        this.container = container;
        sceneManager.init(gc);
    }

    @Override
    public void update(GameContainer gc, int t) throws SlickException {
        // TODO Auto-generated method stub
        this.sceneManager.update(gc, t);
    }
    
//    @Override
//    public void keyPressed(int key, char c) {
//        container.exit();
//    }
    
    private void handleKeyPress(GameContainer gc) {
//        Input input = gc.getInput();
//        
//        if (input.isKeyPressed(ANoteKey)) {
//            notesOnScreen.remove();
//        }
//        List<Integer> keys = new ArrayList<Integer>();
//        keys.add(ANoteKey); keys.add(BNoteKey); keys.add(CNoteKey); keys.add(DNoteKey);
//        keys.add(ENoteKey); keys.add(FNoteKey); keys.add(GNoteKey);
//        if ( keys.stream().map(aKey -> input.isKeyPressed(aKey)).reduce(false,
//                (isPressed1, isPressed2) -> isPressed1 || isPressed2) ) {
//            notesOnScreen.remove();
//        }
    }
}
