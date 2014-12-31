package game.scenes.change_controls;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import music.Handedness;
import music.Note;
import game.Renderable;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.pop_ups.PopUp;
import game.scenes.Scene;
import game.settings.Controls;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

public class ChangeControls extends Scene {
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private final NoteOrderer noteOrderer = NoteOrderer.noteOrderer();
    private final List<Label<String>> controlLabels = new ArrayList<Label<String>>();
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        renderables.stream().forEach( renderable -> renderable.render(g) );
        super.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        super.update(t);
        renderables.stream().forEach( renderable -> renderable.update(t) );
        controlLabels.stream().forEach( label -> label.update(t) );
    }

    @Override
    public Scene parentScene() {
        return Scene.mainMenu();
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        final String[] noteLetters = new String[] { "A", "B", "C", "D", "E", "F", "G", "S" };
        // put in all buttons
        final float yIncrement = 0.05f;
        // single voice controls
        final float singleVoiceX = 0.25f;
        final float singleVoiceInitialY = 0.3f;
        for ( int i=0; i<noteLetters.length; i++ ) {
            float yPosition = singleVoiceInitialY + i * yIncrement;
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, singleVoiceX, yPosition, 
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
            float yPosition = doubleVoiceInitialY + i * yIncrement;
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, doubleVoiceLeftX, yPosition, 
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
            float yPosition = doubleVoiceInitialY + i * yIncrement;
            String noteLetter = noteLetters[i];
            renderables.add(
                Button.textButton( noteLetter, doubleVoiceRightX, yPosition, 
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
        renderables.add( Label.textLabel("Single Hand Controls", 0.25f, 0.2f, Color.orange, secondaryFont ) );
        renderables.add( Label.textLabel("Double Hand Controls", 0.75f, 0.2f, Color.orange, secondaryFont ) );
        // tertiary section labels
        UnicodeFont tertiaryFont = GameFonts.ARIAL_PLAIN_24;
        renderables.add( Label.textLabel("Left", 2f/3, 0.25f, Color.blue, tertiaryFont ) );
        renderables.add( Label.textLabel("Right", 5f/6, 0.25f, Color.blue, tertiaryFont ) );
        
        // put in note orderer
        renderables.add(noteOrderer);
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }
}
