package game.buttons;

import java.io.FileNotFoundException;

import game.scenes.Scene;
import game.scenes.SceneManager;


import music.Instrument;
import music.Music;
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
                Music music = musicFile.getMusic();
                if ( music.isMultiInstrument() ) {
                    SceneManager.setNewScene( Scene.instrumentSelection( musicFile.getMusic() ) );
                } else {
                    Instrument selectedInstrument = music.voices().get(0).instrument();
                    SceneManager.setNewScene( Scene.round( music, selectedInstrument ));
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                System.err.println("File cannot be found");
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
    
    @Override
    public void moveLeft(float fractionX) {
        // TODO Auto-generated method stub
        textComponent.moveLeft( fractionX );
    }

    @Override
    public void moveRight(float fractionX) {
        // TODO Auto-generated method stub
        textComponent.moveRight( fractionX );
    }

    @Override
    public void moveDown(float fractionY) {
        // TODO Auto-generated method stub
        textComponent.moveDown( fractionY );
    }

    @Override
    public void moveUp(float fractionY) {
        // TODO Auto-generated method stub
        textComponent.moveUp( fractionY );
    }
}
