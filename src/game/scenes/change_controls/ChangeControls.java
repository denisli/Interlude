package game.scenes.change_controls;

import java.util.ArrayList;
import java.util.List;

import music.Note;
import game.GameObject;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.pop_ups.PopUp;
import game.scenes.Scene;
import game.settings.Controls;
import game.settings.Handedness;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;

public class ChangeControls extends Scene {
    private final List<GameObject> renderables = new ArrayList<GameObject>();
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
        final String[] noteLetters = new String[] { "A", "B", "C", "D", "E", "F", "G" };
        final String colon = ": ";
        // put in all buttons and labels for controls
        final Color noteKeyNameColor = Color.lightGray;
        final float labelXShift = 0.05f;
        final float yIncrement = 0.05f;
        final float[] xPositions = { 0.25f, 2f/3, 5f/6 };
        final float[] initialYs = { 0.3f, 0.35f, 0.35f };
        final Handedness[] handednesses = { Handedness.SINGLE, Handedness.LEFT, Handedness.RIGHT };
        
        for ( int idx = 0; idx < 3; idx++ ) {
            for ( int i=0; i<noteLetters.length; i++ ) {
                float initialY = initialYs[idx];
                float xPosition = xPositions[idx];
                Handedness handedness = handednesses[idx];
                float yPosition = initialY + i * yIncrement;
                String noteLetter = noteLetters[i];
                int noteInteger = Note.toInteger(noteLetter);
                int noteKeyCode = Controls.correspondingKey(noteInteger,handedness);
                String noteKeyName = Input.getKeyName(noteKeyCode);
                if (noteKeyName.equals("SEMICOLON")) { noteKeyName = ";"; }
                Label<String> keyNameOfNoteLabel = Label.textLabel(noteKeyName, xPosition + labelXShift/2, yPosition, noteKeyNameColor, GameFonts.ARIAL_PLAIN_36);
                renderables.add(keyNameOfNoteLabel);
                renderables.add(
                    Button.textButton( noteLetter+colon, xPosition - labelXShift/2, yPosition, 
                        (Runnable) () -> {
                            PopUp popUp = ChangeControlPopUp.makePopUp( Note.toInteger(noteLetter), handedness, keyNameOfNoteLabel );
                            popUp.addOn( ChangeControls.this );
                        }
                    )
                );
            }
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

}
