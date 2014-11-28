package game.scenes;

import game.Controls;
import game.Game;
import game.MovingNote;

import java.util.LinkedList;
import java.util.Queue;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import music.Instrument;
import music.Note;

public class SongSelection implements Scene {
    private String name;
    private String songTitle;
    private Instrument instrument;
    private Controls controls;
    
    private Queue<MovingNote> notesOnScreen = new LinkedList<MovingNote>();
    
    public SongSelection(String name, String songTitle, Instrument instrument, Controls controls) {
        this.name = name;
        this.songTitle = songTitle;
        this.instrument = instrument;
        this.controls = controls;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        // TODO Auto-generated method stub
        for (MovingNote movingNote : notesOnScreen) {
            movingNote.render(gc, g);
        }
    }

    @Override
    public void update(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        Input input = gc.getInput();
        if (input.isKeyPressed(controls.getANoteKey())) {
            if (!notesOnScreen.isEmpty()) {
                MovingNote movingNote = notesOnScreen.remove();
                Note note = movingNote.note();
                instrument.playNote( note.noteInOctave(controls.getANoteKey()) );
            }
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getBNoteKey()) );
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getCNoteKey()) );
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getDNoteKey()) );
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getENoteKey()) );
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getFNoteKey()) );
        } else if (input.isKeyPressed(controls.getBNoteKey())) {
            MovingNote movingNote = notesOnScreen.remove();
            Note note = movingNote.note();
            instrument.playNote( note.noteInOctave(controls.getGNoteKey()) );
        }
        
        for (MovingNote movingNote : notesOnScreen) {
            movingNote.update(gc, t);
        }
    }
    
    @Override
    public String name() {
        return name;
    }

    @Override
    public Scene nextScene(GameContainer gc, int t) {
        // TODO Auto-generated method stub
        return null;
    }
}
