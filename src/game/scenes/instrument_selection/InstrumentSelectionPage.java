package game.scenes.instrument_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.Renderable;
import game.buttons.Button;
import game.pop_ups.PopUp;
import game.scenes.Scene;
import game.scenes.SceneManager;
import music.Instrument;
import music.InstrumentPiece;
import music.Music;
import music.Voice;

import org.newdawn.slick.Graphics;

public class InstrumentSelectionPage extends Scene {
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private final Music music;
    private Optional<Instrument> selectedInstrument;
    
    public InstrumentSelectionPage( Music music ) {
        this.music = music;
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.songSelection();
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        // TODO Auto-generated method stub
        List<InstrumentPiece> instrumentPieces = music.instrumentPieces();
        int numVoices = instrumentPieces.size();
        for ( int i = 0; i < numVoices; i++ ) {
            InstrumentPiece instrumentPiece = instrumentPieces.get(i);
            renderables.add( Button.textButton( instrumentPiece.instrument().getInstrumentName(), 0.5f, ((float) (i+1)) / (numVoices + 1), (Runnable) () -> {
                selectedInstrument = Optional.of(instrumentPiece.instrument());
            }));
        }
        
        renderables.add( Button.textButton("OK!", 0.5f, 0.9f, (Runnable) () -> {
            SceneManager.setNewScene( Scene.round(music, selectedInstrument.get()) );
        }));
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }

}
