package game.scenes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Controls;
import game.Interlude;
import game.InterludeGame;
import game.buttons.Button;
import music.Instrument;
import music.Music;
import music.MusicFile;
import music.Note;
import music.Parser;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage implements Scene {
    private final String[] songTitles = new String[] { "res/music.txt" };
    private List<Button> buttons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f, 0.1f)));
    
    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for (Button button : buttons) {
            button.update(t);
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        float fractionY = 0.1f;
        for (String songTitle : songTitles) {
            Button button;
            button = Button.songSelectionButton( new MusicFile( songTitle, new File(songTitle) ), 0.5f, fractionY );
            button.init();
            buttons.add( button );
            fractionY += 0.1;
        }
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
