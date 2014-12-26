package game.scenes;

import game.Interlude;
import game.fonts.GameFonts;
import game.pop_ups.PopUp;
import music.LoadSynthesizer;
import music.LoadSynthesizer;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class InitializationScene extends Scene {
    private int loadingTime = 3000; // seconds 
    private boolean loadingScreenUp = false;
    private Image eighthNote;
    private Image interlude;
    
    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int eighthNoteWidth = eighthNote.getWidth();
        int eighthNoteHeight = eighthNote.getHeight();
        int eighthNoteCenterX = containerWidth / 2;
        int eighthNoteCenterY = 2 * containerHeight / 3;
        int interludeWidth = interlude.getWidth();
        int interludeHeight = interlude.getHeight();
        int interludeCenterX = containerWidth / 2;
        int interludeCenterY = containerHeight / 3;
        eighthNote.draw( eighthNoteCenterX - eighthNoteWidth / 2, eighthNoteCenterY - eighthNoteHeight / 2 );
        interlude.draw( interludeCenterX - interludeWidth / 2, interludeCenterY - interludeHeight / 2 );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        loadingTime -= t;
        if ( loadingTime <= 0 ) {
            SceneManager.setNewScene( Scene.mainMenu() );
        }
        float interludeAlpha = interlude.getAlpha();
        interlude.setAlpha( interludeAlpha + (3.0f * t) / ( 2.0f * loadingTime ) );
    }

    @Override
    public void init() {
        new Thread(new Runnable() {
            public void run() {
                LoadSynthesizer.loadSynthesizer();
            }
        }).start();
        
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        try {
            eighthNote = new Image("res/images/eighth_note.png");
            Image initialInterlude = new Image("res/images/interlude.png");
            int initialInterludeWidth = initialInterlude.getWidth();
            interlude = initialInterlude.getScaledCopy( Math.min(1, (float) (containerWidth) / initialInterludeWidth) );
            interlude.setAlpha(0);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

}
