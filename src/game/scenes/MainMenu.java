package game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.InterludeGame;
import game.buttons.Button;
import game.labels.Label;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class MainMenu implements Scene {
    private final List<Button> buttons = Arrays.asList(Button.playButton(0.5f,0.5f),Button.instructionsButton(0.5f,0.6f),Button.controlsButton(0.5f,0.7f));
    private String name = "Main Menu";
    private final List<Label> labels = Arrays.asList(Label.interludeLabel());
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.render(g);
        }
        for (Label label : labels) {
            label.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.update(t);
        }
        for (Label label : labels) {
            label.update(t);
        }
    }
    
    @Override
    public void init() {
        for (Button button : buttons) {
            button.init();
        }
        for (Label label : labels) {
            label.init();
        }
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        throw new RuntimeException("Main menu has no parent scene!");
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
