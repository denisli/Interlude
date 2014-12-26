package game.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.buttons.Button;
import game.pop_ups.PopUp;
import game.scrollers.Scroller;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage extends Scene {
    private Scroller songSelectionScroller = Scroller.songSelectionScroller();
    private List<Button> generalButtons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f, 0.05f)));

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (Button button : generalButtons) {
            button.render(g);
        }
        songSelectionScroller.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for (Button button : generalButtons) {
            button.update(t);
        }
        
        songSelectionScroller.update(t);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        input.addMouseListener( songSelectionScroller );
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

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        input.removeMouseListener( songSelectionScroller );
    }
}
