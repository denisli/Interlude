package game.scenes.initialization_scene;

import game.Interlude;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.LoadSynthesizer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class InitializationScene extends Scene {
    private int loadingTime = 3000; // seconds 
    private Image eighthNote;
    private Image interlude;

    @Override
    public Scene parentScene() {
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
        loadingTime -= t;
        if ( loadingTime <= 0 ) {
            SceneManager.setNewScene( Scene.mainMenu() );
        }
        float interludeAlpha = interlude.getAlpha();
        interlude.setAlpha( interludeAlpha + (3.0f * t) / ( 2.0f * loadingTime ) );
    }

    @Override
    public void init() {
        super.init();
        new Thread(new Runnable() {
            public void run() {
                LoadSynthesizer.loadSynthesizer();
            }
        }).start();
    }

    @Override
    public void cleanUp() {
        
    }

    @Override
    protected void layout() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();

        try {
            eighthNote = new Image("images/eighth_note.png");
            Image initialInterlude = new Image("images/interlude.png");
            int initialInterludeWidth = initialInterlude.getWidth();
            interlude = initialInterlude.getScaledCopy( Math.min(1, (float) (containerWidth) / initialInterludeWidth) );
            interlude.setAlpha(0);
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleServerMessages() {
        
    }

}
