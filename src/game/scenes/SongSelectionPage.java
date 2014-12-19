package game.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.buttons.Button;
import game.pop_ups.PopUp;
import music.MusicFile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage implements Scene {
    private final String[] songTitles = new String[] { "res/new_music3.txt", "res/music2.txt", "God Knows....mid",
            "res/new_music4.txt", "res/combined.txt", "suzumiya-haruhi-no-yuuutsu-bouken-desho-desho.mid"};
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

    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }
}
