package game.pop_ups;


import game.Interlude;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.SceneManager;
import game.settings.Controls;
import music.Handedness;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class ChangeControlPopUp extends PopUp implements KeyListener {
    private final int note;
    private final Handedness handedness;
    private boolean keyPressed = false;
    private int pressedKey;
    private final Label<String> instructionLabel = Label.textLabel("Press the button you want to change control to", 0.5f, 0.5f, Color.black,
            GameFonts.ARIAL_PLAIN_18 );
    
    public ChangeControlPopUp( int note, Handedness handedness ) {
        this.note = note;
        this.handedness = handedness;
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
                Controls.setKey( note, handedness, pressedKey );
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
