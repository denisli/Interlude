package game.scenes.instrument_selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.GameObject;
import game.buttons.Button;
import game.scenes.Scene;
import game.scenes.SceneManager;
import game.selectables.Selectable;
import game.selectables.Statement;
import music.Instrument;
import music.Music;
import music.Voice;

import org.newdawn.slick.Graphics;

public class InstrumentSelectionPage extends Scene {
    private final List<GameObject> renderables = new ArrayList<GameObject>();
    private final Music music;
    private Optional<Instrument> selectedInstrument = Optional.empty();
    
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
        // add in names of the instruments to play
        List<Voice> voices = music.voices();
        int numInstruments = voices.size();
        for ( int i = 0; i < numInstruments; i++ ) {
            Voice voice = voices.get(i);
            renderables.add( Selectable.textSelectable( voice.instrument().getInstrumentName(), 0.5f, ((float) (i+1)) / (numInstruments + 1), new Statement() { 
                public boolean isTrue() {
                    if ( selectedInstrument.isPresent() ) {
                        return selectedInstrument.get().equals( voice.instrument() );
                    } else {
                        return false;
                    }
                }
            }, (Runnable) () -> { selectedInstrument = Optional.of( voice.instrument() ); } ));
        }
        
        // add in buttons
        renderables.add( Button.textButton("OK!", 0.8f, 0.9f, (Runnable) () -> {
            if ( selectedInstrument.isPresent() ) {
                SceneManager.setNewScene( Scene.round(music, selectedInstrument.get()) );
            } else {
                
            }
        }));
        renderables.add( Button.backButton( 0.9f, 0.1f ) );
    }

}
