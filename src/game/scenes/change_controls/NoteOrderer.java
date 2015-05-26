package game.scenes.change_controls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import util.Slotter;
import game.settings.Controls;
import game.Interlude;
import game.Renderable;
import game.Updateable;

public class NoteOrderer implements Renderable, Updateable {
    private static final List<Integer> NOTES_IN_ORDER = Controls.notesInOrder();
    private static final int NUM_NOTES = NOTES_IN_ORDER.size();
    private static final float FRACTION_Y = 0.8f;
    private final List<NoteOrdererDraggable> draggables = new ArrayList<NoteOrdererDraggable>();
    private Optional<NoteOrdererDraggable> draggedNote = Optional.empty();
    private Slotter<NoteOrdererDraggable> slotter;
    private float[] indexToFractionX = new float[NUM_NOTES];
    private float newFractionX;
    private float newFractionY;
    private float increment;
    private int draggedIdx;
    
    public static NoteOrderer noteOrderer() {
        NoteOrderer noteOrderer = new NoteOrderer();
        noteOrderer.init();
        return noteOrderer;
    }
    
    @Override
    public void render(Graphics g) {
        draggables.stream().forEach( draggable -> draggable.render(g) );
    }

    @Override
    public void update(int t) {
        Input input = Interlude.GAME_CONTAINER.getInput();
        
        draggables.stream().forEach( draggable -> draggable.update(t) );
        
        positionDraggableNotes();
        setDraggedNote( input );
    }
    
    private void positionDraggableNotes() {
        if ( draggedNote.isPresent() ) {
            NoteOrdererDraggable dragged = draggedNote.get();
            for ( int i = 0; i < indexToFractionX.length; i++ ) {
                if ( i == draggedIdx ) { continue; }
                float fractionX = indexToFractionX[i];
                if ( dragged.closeEnough( fractionX, FRACTION_Y ) ) {
                    slotter.push(i);
                    newFractionX = indexToFractionX[i];
                    Controls.changeNoteOrder( draggedIdx, i);
                    draggedIdx = i;
                    
                    // reset position
                    for ( int j = 0; j < indexToFractionX.length; j++ ) {
                        NoteOrdererDraggable draggable = slotter.get(j);
                        if ( draggable == dragged ) { continue; }
                        draggable.setPosition( indexToFractionX[j], FRACTION_Y );
                    }
                    continue;
                }
            }
        }
    }
    
    private void setDraggedNote( Input input ) {
        int index = 0;
        for ( NoteOrdererDraggable draggable : slotter.elements() ) {
            if ( draggable.isDragged( input ) ) {
                if ( !draggedNote.isPresent() ) {
                    newFractionX = indexToFractionX[index];
                    newFractionY = FRACTION_Y;
                    draggedNote = Optional.of( draggable );
                    draggedIdx = index;
                    slotter.lift( index );
                }
                return;
            }
            index++;
        }
        if ( draggedNote.isPresent() ) {
            draggedNote.get().setPosition(newFractionX, newFractionY);
        }
        slotter.drop();
        draggedNote = Optional.empty();
    }

    @Override
    public void init() {
        float initialFractionX = 0.2f;
        increment = ( 1.0f - 2 * initialFractionX ) / (NUM_NOTES - 1);
        for ( int i = 0; i < NOTES_IN_ORDER.size(); i++ ) {
            int noteInteger = NOTES_IN_ORDER.get(i);
            float position = initialFractionX + i * increment;
            indexToFractionX[i] = position;
            draggables.add( NoteOrdererDraggable.draggable( noteInteger, position, FRACTION_Y ) );
        }
        slotter = new Slotter<NoteOrdererDraggable>(draggables);
    }
    
}
