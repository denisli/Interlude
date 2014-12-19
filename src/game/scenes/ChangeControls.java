package game.scenes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import music.Note;
import game.VoiceType;
import game.Renderable;
import game.SimpleFont;
import game.buttons.Button;
import game.labels.Label;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

public class ChangeControls implements Scene {
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private final List<PopUp> popUps = new ArrayList<PopUp>();
    private final List<PopUp> popUpsToBeRemoved = new ArrayList<PopUp>();
    
    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( Renderable renderable : renderables ) {
            renderable.render(g);
        }
        for ( PopUp popUp : popUps ) {
            popUp.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for ( Renderable renderable : renderables ) {
            renderable.update(t);
        }
        for ( PopUp popUp : popUps ) {
            popUp.update(t);
        }
        for ( PopUp popUp : popUpsToBeRemoved) {
            popUps.remove(popUp);
        }
    }

    @Override
    public void init() {
        // first add all the renderables to the list of renderables
        final String[] noteLetters = new String[] { "A", "B", "C", "D", "E", "F", "G", "S" };
        final float yIncrement = 0.05f;
        // single voice controls
        final float singleVoiceX = 0.25f;
        final float singleVoiceInitialY = 0.3f;
        for ( int i=0; i<noteLetters.length; i++ ) {
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, singleVoiceX, singleVoiceInitialY + i * yIncrement, 
                    (Runnable) () -> {
                        PopUp popUp = PopUp.changeControl( Note.toLetter(noteLetter), VoiceType.SINGLE, ChangeControls.this );
                        popUp.addOn( ChangeControls.this );
                    }
                )
            );
        }
        // double voice left controls
        final float doubleVoiceLeftX = 2f/3;
        final float doubleVoiceInitialY = 0.35f;
        for ( int i=0; i<noteLetters.length; i++ ) {
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, doubleVoiceLeftX, doubleVoiceInitialY + i * yIncrement, 
                    (Runnable) () -> {
                        PopUp popUp = PopUp.changeControl( Note.toLetter(noteLetter), VoiceType.LEFT, ChangeControls.this );
                        popUp.addOn( ChangeControls.this );
                    }
                )
            );
        }
        // double voice right controls
        final float doubleVoiceRightX = 5f/6;
        for ( int i=0; i<noteLetters.length; i++ ) {
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, doubleVoiceRightX, doubleVoiceInitialY + i * yIncrement, 
                    (Runnable) () -> {
                        PopUp popUp = PopUp.changeControl( Note.toLetter(noteLetter), VoiceType.RIGHT, ChangeControls.this );
                        popUp.addOn( ChangeControls.this );
                    }
                )
            );
        }
        // general purpose buttons
        renderables.add(Button.backButton(0.9f,  0.1f));
        // secondary section labels
        UnicodeFont secondaryFont = SimpleFont.retrieve("Arial", Font.PLAIN, 32);
        renderables.add( new Label("Single Voice Controls", 0.25f, 0.2f, Color.cyan, secondaryFont ) );
        renderables.add( new Label("Double Voice Controls", 0.75f, 0.2f, Color.cyan, secondaryFont ) );
        // tertiary section labels
        UnicodeFont tertiaryFont = SimpleFont.retrieve("Arial", Font.PLAIN, 24);
        renderables.add( new Label("Left", 2f/3, 0.25f, Color.blue, tertiaryFont ) );
        renderables.add( new Label("Right", 5f/6, 0.25f, Color.blue, tertiaryFont ) );
        
        // initialize all renderables
        for ( Renderable renderable : renderables ) {
            renderable.init();
        }
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }
    
    @Override
    public void fireActivatedButtons() {
        return;
    }

    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        popUps.add(popUp);
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        popUpsToBeRemoved.add(popUp);
    }
}
