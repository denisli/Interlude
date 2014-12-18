package game.pop_ups;

import game.Controls;
import game.Interlude;
import game.VoiceType;
import game.scenes.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

public class ChangeControlPopUp extends PopUp implements KeyListener {
    private final int note;
    private final VoiceType voiceType;
    private boolean keyPressed = false;
    private int pressedKey;
    
    public ChangeControlPopUp( int note, VoiceType voiceType, Scene scene ) {
        super( scene );
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
        // TODO: Change the place where the string is drawn;
        g.drawString( "Press the button you want to change control to", containerWidth / 2.5f, containerHeight / 2.5f);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        if ( keyPressed ) {
            if ( !input.isKeyPressed(pressedKey) ) {
                Controls.setKey( note, voiceType, pressedKey );
                Interlude.GAME_CONTAINER.getInput().removeKeyListener(this);
                this.remove( scene() );
            }
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        Interlude.GAME_CONTAINER.getInput().addKeyListener(this);
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
