package game.scenes;

import game.Controls;
import game.MovingSound;
import game.buttons.Button;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import music.Instrument;
import music.Music;
import music.MusicElement;
import music.Simultaneous;
import music.SoundElement;
import music.Rest;
import music.Note;
import music.Voice;

public class Round implements Scene {
    private final Music music;
    private final Controls controls;
    private final List<Button> buttons = new ArrayList<Button>();
    private int restingTime = 4000;
    
    private Voice voice;
    private Instrument instrument;
    
    private Queue<MovingSound> notesOnScreen = new LinkedList<MovingSound>();
    
    public Round(Music music, Controls controls ) {
        this.music = music;
        this.controls = controls;
        voice = music.voices().get(0);
        instrument = voice.instrument();
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        for (MovingSound movingSound : notesOnScreen) {
            movingSound.render(gc, g);
        }
        for (Button button : buttons) {
            button.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        Input input = gc.getInput();
        for ( int key : controls.noteKeys() ) {
            if ( input.isKeyPressed(key) ) {
                if ( !notesOnScreen.isEmpty() ) {
                    int letter = controls.correspondingNoteOfKey(key);
                    MovingSound movingSound = notesOnScreen.remove();
                    SoundElement soundElement = movingSound.soundElement();
                    SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                    soundElement.bePlayed(instrument);
                }
            }
        }
        
        restingTime = Math.max(restingTime - t, 0);
        
        if (restingTime == 0) { // pick out another note!
            if (voice.ended()) {
                // go to a different scene?
            } else {
                MusicElement element = voice.next();
                
                SoundElement soundElement = (SoundElement) element;
                MovingSound movingSound = new MovingSound( soundElement );
                movingSound.init(gc);
                if (!voice.ended()) {
                    restingTime = voice.timeUntilNextElement();
                }
                notesOnScreen.add( movingSound );
            }
        }
        
        if ( !notesOnScreen.isEmpty() ) { 
            while ( notesOnScreen.peek().offScreen() ) {
                notesOnScreen.remove();
                if (notesOnScreen.isEmpty()) {
                    break;
                }
            }
        }
        
        for ( MovingSound movingSound : notesOnScreen ) {
            movingSound.update(gc, t);
        }
    }
    
    @Override
    public void init(GameContainer gc) {
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            Button button = Button.noteButton(note);
            button.init(gc);
            buttons.add( button );
        }
    }
    
    @Override
    public String name() {
        return null;
    }

    @Override
    public Scene nextScene(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        return this;
    }
}
