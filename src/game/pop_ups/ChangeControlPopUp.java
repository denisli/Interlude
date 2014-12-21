package game.pop_ups;

import java.awt.Font;

import game.Controls;
import game.Interlude;
import game.SimpleFont;
import game.VoiceType;
import game.labels.Label;
import game.scenes.Scene;
import game.scenes.SceneManager;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class ChangeControlPopUp extends PopUp implements KeyListener {
    private final int note;
    private final VoiceType voiceType;
    private boolean keyPressed = false;
    private int pressedKey;
    private final Label instructionLabel = Label.label("Press the button you want to change control to", 0.5f, 0.5f, Color.black,
            SimpleFont.retrieve( "Arial", Font.PLAIN, 18) );
    
    public ChangeControlPopUp( int note, VoiceType voiceType ) {
        this.note = note;
        this.voiceType = voiceType;
    }
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        g.setColor( Color.white );
        g.fillRect( containerWidth / 3, containerHeight / 3, containerWidth / 3, containerHeight / 3 );
        
        g.setColor( Color.black );
        instructionLabel.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        if ( keyPressed ) {
            if ( !input.isKeyPressed(pressedKey) ) {
                Controls.setKey( note, voiceType, pressedKey );
                Interlude.GAME_CONTAINER.getInput().removeKeyListener(this);
                this.remove( SceneManager.currentScene() );
            }
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        Interlude.GAME_CONTAINER.getInput().addKeyListener(this);
        instructionLabel.init();
    }

    @Override
    public void keyPressed(int key, char c) {
        // TODO Auto-generated method stub
        keyPressed = true;
        pressedKey = key;
    }

    @Override
    public void keyReleased(int key, char c) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inputEnded() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void inputStarted() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isAcceptingInput() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void setInput(Input input) {
        // TODO Auto-generated method stub
        
    }
}
