package game.scenes;

import java.util.ArrayList;
import java.util.List;

import game.Controls;
import game.buttons.Button;
import music.Instrument;
import music.Music;
import music.Note;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage implements Scene {
    private final String[] songTitles = new String[] { "God Knows" };
    private List<Button> buttons = new ArrayList<Button>();

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        int yCoord = 0;
        for (String songTitle : songTitles) {
            Button button = Button.songSelectionButton( songTitle, yCoord );
            button.init(gc);
            buttons.add( button );
            yCoord += button.height();
        }
    }

    @Override
    public Scene nextScene(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        Input input = gc.getInput();
        if (buttons.get(0).isClicked(input)) {
            Scene newScene = Scene.round( Music.godKnows(), new Controls() );
            newScene.init(gc);
            return newScene;
        } else {
            return this;
        }
    }
    
    
}
