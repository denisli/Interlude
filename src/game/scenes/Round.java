package game.scenes;

import game.Controls;
import game.MovingNote;
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
import music.Note;
import music.Rest;

public class Round implements Scene {
    private final Music music;
    private final Controls controls;
    private final List<Button> buttons = new ArrayList<Button>();
    private int restingTime = 1500;
    
    private Queue<MovingNote> notesOnScreen = new LinkedList<MovingNote>();
    
    public Round(Music music, Controls controls ) {
        this.music = music;
        this.controls = controls;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        for (MovingNote movingNote : notesOnScreen) {
            movingNote.render(gc, g);
        }
        for (Button button : buttons) {
            button.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        Input input = gc.getInput();
        Instrument instrument = music.instrument();
        if (input.isKeyPressed(controls.getANoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.A ) );
            }
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.B ) );
            }
        } else if (input.isKeyPressed(controls.getCNoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.C ) );
            }
        } else if (input.isKeyPressed(controls.getDNoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.D ) );
            }
        } else if (input.isKeyPressed(controls.getENoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.E ) );
            }
        } else if (input.isKeyPressed(controls.getFNoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.F ) );
            }
        } else if (input.isKeyPressed(controls.getGNoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.correspondingNote( Note.G ) );
            }
        }
        
        restingTime = Math.max(restingTime - t, 0);
        
        if (restingTime == 0) { // pick out another note!
            if (music.ended()) {
                // go to a different scene?
            } else {
                MusicElement element = music.next();
                if ( element instanceof Rest ) {
                    Rest rest = (Rest) element;
                    restingTime = rest.duration();
                } else { // then it must be a note
                    
                    Note note = (Note) element;
                    MovingNote movingNote = new MovingNote(note);
                    movingNote.init(gc);
                    restingTime = note.duration();
                    notesOnScreen.add( movingNote );
                }
            }
        }
        
        List<MovingNote> notesOffScreen = new ArrayList<MovingNote>();
        for (MovingNote movingNote : notesOnScreen) {
            movingNote.update(gc, t);
            if (movingNote.offScreen()) {
                notesOffScreen.add(movingNote);
            }
        }
        for (MovingNote movingNote : notesOffScreen) {
            notesOnScreen.remove(movingNote);
        }
        
        for (Button button : buttons) {
            button.update(gc, t);
        }
    }
    
    @Override
    public void init(GameContainer gc) {
        int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G };
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
