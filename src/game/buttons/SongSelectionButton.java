package game.buttons;

import game.InterludeGame;
import game.scenes.Scene;
import game.scenes.SceneManager;

import java.awt.Font;

import music.Music;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;

public class SongSelectionButton implements Button {
    private final Music music;
    private final TextButton textComponent;
    
    public SongSelectionButton(Music music, float fractionX, float fractionY) {
        this.music = music;
        this.textComponent = new TextButton( music.title(), fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene( Scene.round(music) );
        });
    }
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        textComponent.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        textComponent.update(t);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        textComponent.init();
    }

    @Override
    public int width() {
        // TODO Auto-generated method stub
        return textComponent.width();
    }

    @Override
    public int height() {
        // TODO Auto-generated method stub
        return textComponent.height();
    }

    @Override
    public boolean isClicked(Input input) {
        // TODO Auto-generated method stub
        return textComponent.isClicked(input);
    }

    @Override
    public void setEffect(Runnable effect) {
        // TODO Auto-generated method stub
        return;
    }

    @Override
    public void callEffect() {
        // TODO Auto-generated method stub
        return;
    }
}
