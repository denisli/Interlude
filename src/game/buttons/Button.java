package game.buttons;

import game.Controls;
import game.VoiceType;
import game.InterludeGame;
import game.Renderable;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.Music;
import music.MusicFile;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public interface Button extends Renderable {
    public static Button playButton(float fractionX, float fractionY) {
        Button button = new TextButton("Play", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(Scene.songSelection());
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
    
    public static Button textButton(String text, float fractionX, float fractionY, Runnable effect) {
        return new TextButton( text, fractionX, fractionY, effect );
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
