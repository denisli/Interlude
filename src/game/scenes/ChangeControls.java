package game.scenes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import music.Handedness;
import music.InstrumentType;
import music.Note;
import game.Renderable;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.fonts.SimpleFont;
import game.labels.Label;
import game.note_orderer.NoteOrderer;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

public class ChangeControls extends Scene {
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private final List<PopUp> popUps = new ArrayList<PopUp>();
    private final List<PopUp> popUpsToBeRemoved = new ArrayList<PopUp>();
    private final NoteOrderer noteOrderer = NoteOrderer.noteOrderer();
    

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( Renderable renderable : renderables ) {
            renderable.render(g);
        }
        for ( PopUp popUp : popUps ) {
            popUp.render(g);
        }
        
        noteOrderer.render(g);
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
        
        noteOrderer.update(t);
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
                        PopUp popUp = PopUp.changeControl( Note.toInteger(noteLetter), Handedness.SINGLE );
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
                        PopUp popUp = PopUp.changeControl( Note.toInteger(noteLetter), Handedness.LEFT );
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
                        PopUp popUp = PopUp.changeControl( Note.toInteger(noteLetter), Handedness.RIGHT );
                        popUp.addOn( ChangeControls.this );
                    }
                )
            );
        }
        // general purpose buttons
        renderables.add(Button.backButton(0.9f,  0.1f));
        // secondary section labels
        UnicodeFont secondaryFont = GameFonts.ARIAL_PLAIN_32;
        renderables.add( Label.label("Single Voice Controls", 0.25f, 0.2f, Color.cyan, secondaryFont ) );
        renderables.add( Label.label("Double Voice Controls", 0.75f, 0.2f, Color.cyan, secondaryFont ) );
        // tertiary section labels
        UnicodeFont tertiaryFont = GameFonts.ARIAL_PLAIN_24;
        renderables.add( Label.label("Left", 2f/3, 0.25f, Color.blue, tertiaryFont ) );
        renderables.add( Label.label("Right", 5f/6, 0.25f, Color.blue, tertiaryFont ) );
        
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
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        popUps.add(popUp);
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        popUpsToBeRemoved.add(popUp);
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }
}
