package game.scenes;

import java.util.Arrays;
import java.util.List;

import game.buttons.Button;
import game.buttons.PlayButton;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class MainMenu implements Scene {
    private final List<Button> buttons;
    private String name = "Main Menu";

    public MainMenu() {
        this.buttons = Arrays.asList(Button.playButton());
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.update(gc, t);
        }
    }
    
    @Override
    public void init(GameContainer gc) {
        for (Button button : buttons) {
            button.init(gc);
        }
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return name;
    }

    @Override
    public Scene nextScene(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
