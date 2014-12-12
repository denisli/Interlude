package game.scenes;

import game.Controls;
import game.Hand;
import game.MovingSound;
import game.OneVoiceMovingSound;
import game.TwoVoiceMovingSound;
import game.buttons.Button;

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
    List<Button> buttons = new ArrayList<Button>();
    Queue<MovingSound> leftNotesOnScreen = new LinkedList<MovingSound>();
    Queue<MovingSound> rightNotesOnScreen = new LinkedList<MovingSound>();
    
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
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        for (MovingSound movingSound : leftNotesOnScreen) {
            movingSound.render(gc, g);
        }
        for (MovingSound movingSound : rightNotesOnScreen) {
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
                
                SoundElement soundElement = (SoundElement) element;
                MovingSound movingSound = new OneVoiceMovingSound( soundElement );
                movingSound.init(gc);
                if (!leftVoice.ended()) {
                    leftRestingTime = leftVoice.timeUntilNextElement();
                }
                leftNotesOnScreen.add( movingSound );
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
            movingSound.update(gc, t);
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
                
                SoundElement soundElement = (SoundElement) element;
                MovingSound movingSound = new OneVoiceMovingSound( soundElement );
                movingSound.init(gc);
                if (!rightVoice.ended()) {
                    rightRestingTime = rightVoice.timeUntilNextElement();
                }
                rightNotesOnScreen.add( movingSound );
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
            movingSound.update(gc, t);
        }
    }

    @Override
    public void init(GameContainer gc) {
        // TODO Auto-generated method stub
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G, Simultaneous.S };
        for (int note : notes) {
            Button button = Button.twoVoiceNoteButton(note, Hand.LEFT);
            button.init(gc);
            buttons.add( button );
        }
        for (int note : notes) {
            Button button = Button.twoVoiceNoteButton(note, Hand.RIGHT);
            button.init(gc);
            buttons.add( button );
        }
    }

    @Override
    public Scene nextScene(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        return null;
    }
}
