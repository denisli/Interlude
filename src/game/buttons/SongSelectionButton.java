package game.buttons;

import game.scenes.Scene;
import game.scenes.SceneManager;


import music.MusicFile;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionButton implements Button {
    private final MusicFile musicFile;
    private final TextButton textComponent;
    
    public SongSelectionButton(MusicFile musicFile, float fractionX, float fractionY) {
        this.musicFile = musicFile;
        this.textComponent = new TextButton( musicFile.musicTitle(), fractionX, fractionY, (Runnable) () -> {
            try {
                SceneManager.setNewScene( Scene.round( musicFile.getMusic() ) );
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
        textComponent.setEffect(effect);
    }

    @Override
    public void callEffect() {
        // TODO Auto-generated method stub
        textComponent.callEffect();
    }
}
