package game.scenes.change_controls;


import game.Interlude;
import game.fonts.GameFonts;
import game.labels.Label;
import game.pop_ups.PopUp;
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
    private final Label<String> keyNameLabel;
    private boolean keyPressed = false;
    private int pressedKey;
    private final Label<String> instructionLabel = Label.textLabel("Press the button you want to change control to.", 0.5f, 0.5f, Color.black,
            GameFonts.ARIAL_PLAIN_18 );
    
    public static PopUp makePopUp( int note, Handedness handedness, Label<String> keyNameLabel ) {
        PopUp popUp = new ChangeControlPopUp( note, handedness, keyNameLabel );
        popUp.init();
        return popUp;
    }
    
    public ChangeControlPopUp( int note, Handedness handedness, Label<String> keyNameLabel ) {
        this.note = note;
        this.handedness = handedness;
        this.keyNameLabel = keyNameLabel;
    }
    
    @Override
    public void render(Graphics g) {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int instructionLabelWidth = (int) (instructionLabel.fractionWidth() * containerWidth);
        int instructionLabelHeight = (int) (instructionLabel.fractionHeight() * containerHeight);
        int instructionLabelCenterX = (int) (instructionLabel.fractionX() * containerWidth);
        int instructionLabelCenterY = (int) (instructionLabel.fractionY() * containerHeight);
        int padding = 20;
        g.setColor( Color.orange );
        int matUpperLeftX = instructionLabelCenterX - instructionLabelWidth / 2 - padding;
        int matUpperLeftY = instructionLabelCenterY - instructionLabelHeight / 2 - padding;
        g.fillRoundRect( matUpperLeftX, matUpperLeftY, instructionLabelWidth + 2 * padding, instructionLabelHeight + 2 * padding, padding);
        g.setColor( Color.red );
        instructionLabel.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        if ( keyPressed ) { // if key was pressed...
            if ( !input.isKeyPressed(pressedKey) ) { // but no longer pressed
                String keyName = Input.getKeyName(pressedKey); 
                if ( keyName.equals("SEMICOLON")) { keyName = ";"; }
                keyNameLabel.updateValue(keyName);
                Controls.setKey( note, handedness, pressedKey );
                Interlude.GAME_CONTAINER.getInput().removeKeyListener(this);
                this.remove( SceneManager.currentScene() );
            }
        }
    }

    @Override
    public void init() {
        Interlude.GAME_CONTAINER.getInput().addKeyListener(this);
        instructionLabel.init();
    }

    @Override
    public void keyPressed(int key, char c) {
        keyPressed = true;
        pressedKey = key;
    }

    @Override
    public void keyReleased(int key, char c) {
        
    }

    @Override
    public void inputEnded() {
        
    }

    @Override
    public void inputStarted() {
        
    }

    @Override
    public boolean isAcceptingInput() {
        return true;
    }

    @Override
    public void setInput(Input input) {
        
    }
}
