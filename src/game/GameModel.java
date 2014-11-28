package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class GameModel {
    private int ANoteKey = Input.KEY_A;
    private int BNoteKey = Input.KEY_S; 
    private int CNoteKey = Input.KEY_D;
    private int DNoteKey = Input.KEY_F;
    private int ENoteKey = Input.KEY_J;
    private int FNoteKey = Input.KEY_K;
    private int GNoteKey = Input.KEY_L;
    
    private Queue<Integer> notesOnScreen = new LinkedList<Integer>();
    
    public boolean notePressed(GameContainer gc) {
        Input input = gc.getInput();
        List<Integer> keys = new ArrayList<Integer>();
        keys.add(ANoteKey); keys.add(BNoteKey); keys.add(CNoteKey); keys.add(DNoteKey);
        keys.add(ENoteKey); keys.add(FNoteKey); keys.add(GNoteKey);
        return keys.stream().map(key -> input.isKeyPressed(key)).reduce(false,
                (isPressed1, isPressed2) -> isPressed1 || isPressed2);
    }
    
}
