package game.buttons;

import game.Renderable;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.MusicFile;

import org.newdawn.slick.Input;

public interface Button extends Renderable {
    public static Button playButton(float fractionX, float fractionY) {
        Button button = new TextButton("Play", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(Scene.songSelection());
        });
        button.init();
        return button;
    }
    
    public static Button playWithFriendsButton( float fractionX, float fractionY ) {
        Button button = new TextButton("Play with friends!", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(Scene.connectWithFriends());
        });
        button.init();
        return button;
    }
    
    public static Button instructionsButton(float fractionX, float fractionY) {
        Button button = new TextButton("Instructions", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(Scene.instructionsPage());
        });
        button.init();
        return button;
    }
    
    public static Button controlsButton(float fractionX, float fractionY) {
        Button button = new TextButton("Controls", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(Scene.changeControlsPage());
        });
        button.init();
        return button;
    }
    
    public static Button backButton(float fractionX, float fractionY) {
        Button button = new TextButton("Back", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(SceneManager.currentScene().parentScene());
        });
        button.init();
        return button;
    }
    
    public static Button startConnectionButton(float fractionX, float fractionY) {
        Button button = new TextButton("Start Connection", fractionX, fractionY, (Runnable) () -> {
            throw new RuntimeException("Ah, need to implement this!");
        });
        button.init();
        return button;
    }
    
    public static Button textButton(String text, float fractionX, float fractionY, Runnable effect) {
        Button button = new TextButton( text, fractionX, fractionY, effect );
        button.init();
        return button;
    }
    
    public static Button songSelectionButton(MusicFile musicFile, float fractionX, float fractionY) {
        Button button = new SongSelectionButton( musicFile, fractionX, fractionY );
        button.init();
        return button;
    }
    
    public void setEffect(Runnable effect);
    
    public void callEffect();
    
    public boolean isClicked(Input input);
    
    public int width();
    
    public int height();
}
