package game.scenes;

import java.util.ArrayList;
import java.util.List;

import game.Interlude;
import game.InterludeGame;
import game.buttons.Button;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Instructions implements Scene {
    private int upperLeftX;
    private int upperLeftY;
    private final List<Button> buttons = new ArrayList<Button>();

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        g.drawString("Your goal is to hit the notes as close as possible to the circle at the end. Have fun!", upperLeftX, upperLeftY);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        upperLeftX = containerHeight / 2;
        upperLeftY = containerWidth / 2;
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }
    
    @Override
    public void fireActivatedButtons() {
        Input input = Interlude.GAME_CONTAINER.getInput();
        for (Button button : buttons) {
            if (button.isClicked(input)) {
                button.callEffect();
            }
        }
    }
}
