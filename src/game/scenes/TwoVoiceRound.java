package game.scenes;

import game.Controls;
import game.Hand;
import game.Interlude;
import game.MovingSound;
import game.OneVoiceMovingSound;
import game.TwoVoiceMovingSound;
import game.buttons.Button;
import game.InterludeGame;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import music.Music;
import music.MusicElement;
import music.Note;
import music.Simultaneous;
import music.SoundElement;
import music.Voice;

public class TwoVoiceRound extends Round {
    private int leftRestingTime = 4000;
    private int rightRestingTime = 4000;
    private final Voice rightVoice;
    private final Voice leftVoice;
    private final List<Button> buttons = new ArrayList<Button>();
    private final Queue<MovingSound> leftNotesOnScreen = new LinkedList<MovingSound>();
    private final Queue<MovingSound> rightNotesOnScreen = new LinkedList<MovingSound>();
    
    public TwoVoiceRound( Music music ) {
        super(music);
        List<Voice> voices = music.voices();
        this.rightVoice = voices.get(0);
        this.leftVoice = voices.get(1);
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for (MovingSound movingSound : leftNotesOnScreen) {
            movingSound.render(g);
        }
        for (MovingSound movingSound : rightNotesOnScreen) {
            movingSound.render(g);
        }
        for (Button button : buttons) {
            button.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        Input input = InterludeGame.gameContainer().getInput();
        for ( int key : Controls.leftNoteKeys() ) {
            if ( input.isKeyPressed(key) ) {
                if ( !leftNotesOnScreen.isEmpty() ) {
                    int letter = Controls.correspondingNote(key);
                    MovingSound movingSound = leftNotesOnScreen.remove();
                    SoundElement soundElement = movingSound.soundElement();
                    SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                    correspondingSoundElement.bePlayed(leftVoice.instrument());
                }
            }
        }
        
        leftRestingTime = Math.max(leftRestingTime - t, 0);
        
        if (leftRestingTime == 0) { // pick out another note!
            if (leftVoice.ended()) {
                // go to a different scene?
            } else {
                MusicElement element = leftVoice.next();
                if ( element.isRest() ) {
                    if (!leftVoice.ended()) {
                        leftRestingTime = leftVoice.timeUntilNextElement();
                    }
                } else {
                    SoundElement soundElement = (SoundElement) element;
                    MovingSound movingSound = new TwoVoiceMovingSound( soundElement, Hand.LEFT );
                    movingSound.init();
                    if (!leftVoice.ended()) {
                        leftRestingTime = leftVoice.timeUntilNextElement();
                    }
                    leftNotesOnScreen.add( movingSound );
                }
            }
        }
        
        if ( !leftNotesOnScreen.isEmpty() ) { 
            while ( leftNotesOnScreen.peek().offScreen() ) {
                leftNotesOnScreen.remove();
                if (leftNotesOnScreen.isEmpty()) {
                    break;
                }
            }
        }
        
        for ( MovingSound movingSound : leftNotesOnScreen ) {
            movingSound.update(t);
        }
        
        for ( int key : Controls.rightNoteKeys() ) {
            if ( input.isKeyPressed(key) ) {
                if ( !rightNotesOnScreen.isEmpty() ) {
                    int letter = Controls.correspondingNote(key);
                    MovingSound movingSound = rightNotesOnScreen.remove();
                    SoundElement soundElement = movingSound.soundElement();
                    SoundElement correspondingSoundElement = soundElement.correspondingSoundElement(letter);
                    correspondingSoundElement.bePlayed(rightVoice.instrument());
                }
            }
        }
        
        rightRestingTime = Math.max(rightRestingTime - t, 0);
        
        if (rightRestingTime == 0) { // pick out another note!
            if (rightVoice.ended()) {
                // go to a different scene?
            } else {
                MusicElement element = rightVoice.next();
                if ( element.isRest() ) {
                    if (!rightVoice.ended()) {
                        rightRestingTime = rightVoice.timeUntilNextElement();
                    }
                } else {
                    SoundElement soundElement = (SoundElement) element;
                    MovingSound movingSound = new TwoVoiceMovingSound( soundElement, Hand.RIGHT );
                    movingSound.init();
                    if (!rightVoice.ended()) {
                        rightRestingTime = rightVoice.timeUntilNextElement();
                    }
                    rightNotesOnScreen.add( movingSound );
                }
            }
        }
        
        if ( !rightNotesOnScreen.isEmpty() ) { 
            while ( rightNotesOnScreen.peek().offScreen() ) {
                rightNotesOnScreen.remove();
                if (rightNotesOnScreen.isEmpty()) {
                    break;
                }
            }
        }
        
        for ( MovingSound movingSound : rightNotesOnScreen ) {
            movingSound.update(t);
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        Controls.enableTwoVoiceControls();
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            Button button = Button.twoVoiceNoteButton(note, Hand.LEFT);
            button.init();
            buttons.add( button );
        }
        for (int note : notes) {
            Button button = Button.twoVoiceNoteButton(note, Hand.RIGHT);
            button.init();
            buttons.add( button );
        }
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void fireActivatedButtons() {
        Input input = Interlude.GAME_CONTAINER.getInput();
        for (Button button : buttons) {
            if (button.isClicked(input)) {
                button.callEffect();
            }
        }
    }
}
