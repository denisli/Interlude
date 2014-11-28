package game;

import game.scenes.Scene;

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

public class Game extends BasicGame {
    private int ANoteKey = Input.KEY_A;
    private int BNoteKey = Input.KEY_S; 
    private int CNoteKey = Input.KEY_D;
    private int DNoteKey = Input.KEY_F;
    private int ENoteKey = Input.KEY_J;
    private int FNoteKey = Input.KEY_K;
    private int GNoteKey = Input.KEY_L;
    
    private Scene scene;
    
    public Game() {
        super("Interlude");
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(GameContainer gc, int t) throws SlickException {
        // TODO Auto-generated method stub
        
        
    }
    
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
