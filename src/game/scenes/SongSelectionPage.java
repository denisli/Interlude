package game.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import game.Interlude;
import game.buttons.Button;
import game.pop_ups.PopUp;
import music.MusicFile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage implements Scene {
    private final String[] songTitles = new String[] { 
            "Little Busters! - Little Busters!", 
            "Hunter X Hunter - Ohayou",
            "Suzumiya Haruhi no Yuuutsu - God Knows",
            "Gundam 00 - Ash Like Snow",
            "Guilty Crown - My Dearest",
            "Suzumiya Haruhi no Yuuutsu - Bouken Desho Desho",
            "Angel Beats - My Soul Your Beats",
            "Bakemonogatari - Kimi no Shiranai Monogatari",
            "Anime Pinao Nantoka 2012",
            "Sword Art Online - Friendly Feelings",
            "Spirited Away - Itsumo Nando Demo",
            "Card Captor Sakura - Arigatou",
            "Sword Art Online - Crossing Field",
            "Rurouni Kenshin - Sobakasu",
    };
    private final String[] fileNames = new String[] { 
            "Little Busters!.mid", 
            "Hunter X Hunter - Ohayou .mid", 
            "God Knows....mid",
            "Gundam 00 - Ash Like Snow.mid", 
            "My Dearest.mid", 
            "suzumiya-haruhi-no-yuuutsu-bouken-desho-desho.mid",
            "angel-beats-my-soul-your-beats.mid", 
            "Bakemonogatari - Kimi no Shiranai Monogatari.mid",
            "Anime Piano Nantoka 2012.mid", 
            "Sword Art Online - Friendly Feelings.mid",
            "Spirited Away - Itsumo Nando Demo.mid", 
            "Card Captor Sakura - Arigatou.mid", 
            "Sword Art Online - Crossing Field.mid",
            "Rurouni Kenshin - Sobakasu.mid" 
            };
    private List<Button> songSelectionButtons = new ArrayList<Button>();
    private int firstIndex; // index of first song to include
    private int lastIndex; // index of last song to include
    private List<Button> generalButtons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f, 0.1f)));
    
    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (Button button : generalButtons) {
            button.render(g);
        }
        
        for ( int index = firstIndex; index <= lastIndex; index++ ) {
            Button songSelectionButton = songSelectionButtons.get(index);
            songSelectionButton.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for (Button button : generalButtons) {
            button.update(t);
        }
        
        for ( Button songSelectionButton : songSelectionButtons ) {
            songSelectionButton.update(t);
        }
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        if ( input.isKeyPressed(Input.KEY_UP) ) {
            if ( firstIndex > 0 ) {
                firstIndex--;
                lastIndex--;
                for ( Button songSelectionButton : songSelectionButtons ) {
                    songSelectionButton.moveDown( 0.1f );
                }
            }
        } else if ( input.isKeyPressed(Input.KEY_DOWN) ) {
            if ( lastIndex < songSelectionButtons.size() - 1 ) {
                firstIndex++;
                lastIndex++;
                for ( Button songSelectionButton : songSelectionButtons ) {
                    songSelectionButton.moveUp( 0.1f );
                }
            }
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        float fractionY = 0.1f;
        for ( int i = 0; i < fileNames.length; i++ ) {
            String songTitle = songTitles[i];
            String fileName = fileNames[i];
            Button button = Button.songSelectionButton( new MusicFile( songTitle, new File("res/midi/" + fileName) ), 0.5f, fractionY );
            button.init();
            songSelectionButtons.add( button );
            fractionY += 0.1;
        }
        firstIndex = 0;
        lastIndex = Math.min(7, songSelectionButtons.size() - 1);
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
