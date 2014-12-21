package game.scenes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.buttons.Button;
import game.labels.Label;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Instructions implements Scene {
    private int upperLeftX;
    private int upperLeftY;
    private final List<Button> buttons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f, 0.1f)));
    private final List<Label> labels = new ArrayList<Label>(Arrays.asList(Label.interludeLabel()));

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        g.setColor(Color.yellow);
        g.drawString("Your goal is to hit the notes as close as possible to the circle at the end. Have fun!", upperLeftX, upperLeftY);
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
        // TODO Auto-generated method stub
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        upperLeftX = containerWidth / 2;
        upperLeftY = containerHeight / 2;
        for (Button button : buttons) {
            button.init();
        }
        for (Label label : labels) {
            label.init();
        }
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }

    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }
}
