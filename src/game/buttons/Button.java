package game.buttons;

import game.Controls;
import game.Hand;
import game.InterludeGame;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.Music;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

public interface Button {
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
        Button button = new TextButton("Controls", fractionX, fractionY);
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
    
    public static Button songSelectionButton(Music music, float fractionX, float fractionY) {
        Button button = new SongSelectionButton( music, fractionX, fractionY );
        button.init();
        return button;
    }
    
    public static Button noteButton(int note) {
        return new OneVoiceNoteButton( note );
    }
    
    public static Button twoVoiceNoteButton(int note, Hand hand) {
        return new TwoVoiceNoteButton( note, hand );
    }
    
    public void setEffect(Runnable effect);
    
    public void callEffect();
    
    public void render(Graphics g);
    
    public void update(int t);
    
    public void init();
    
    public boolean isClicked(Input input);
    
    public int width();
    
    public int height();
}
