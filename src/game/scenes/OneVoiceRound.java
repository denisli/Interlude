package game.scenes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import game.Interlude;
import game.InterludeGame;
import game.Controls;
import game.MovingSound;
import game.OneVoiceMovingSound;
import game.buttons.Button;
import music.Music;
import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.SoundElement;
import music.Voice;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class OneVoiceRound extends Round {
    private int restingTime = 4000;
    private final Voice voice;
    private final List<Button> buttons = new ArrayList<Button>();
    private final Queue<MovingSound> notesOnScreen = new LinkedList<MovingSound>();
    
    public OneVoiceRound( Music music ) {
        super(music);
        this.voice = music().voices().get(0);
    }
    
    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (MovingSound movingSound : notesOnScreen) {
            movingSound.render(g);
        }
        for (Button button : buttons) {
            button.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        for ( int key : Controls.noteKeys() ) {
            if ( input.isKeyPressed(key) ) {
                if ( !notesOnScreen.isEmpty() ) {
                    int letter = Controls.correspondingNote(key);
                    MovingSound movingSound = notesOnScreen.remove();
                    SoundElement soundElement = movingSound.soundElement();
                    SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                    correspondingSoundElement.bePlayed(voice.instrument());
                }
            }
        }
        
        restingTime = Math.max(restingTime - t, 0);
        
        if (restingTime == 0) { // pick out another note!
            if (voice.ended()) {
                // go to a different scene?
            } else {
                MusicElement element = voice.next();
                if ( element.isRest() ) {
                    restingTime = voice.timeUntilNextElement();
                    if (!voice.ended()) {
                        restingTime = voice.timeUntilNextElement();
                    }
                } else {
                    SoundElement soundElement = (SoundElement) element;
                    MovingSound movingSound = new OneVoiceMovingSound( soundElement );
                    movingSound.init();
                    if (!voice.ended()) {
                        restingTime = voice.timeUntilNextElement();
                    }
                    notesOnScreen.add( movingSound );
                }
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
            movingSound.update(t);
        }
    }
    
    @Override
    public void init() {
        Controls.enableSingleVoiceControls();
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            Button button = Button.noteButton(note);
            button.init();
            buttons.add( button );
        }
    }
    
    @Override
    public String name() {
        return null;
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void fireActivatedButtons() {
        // TODO Auto-generated method stub
        
    }
}
