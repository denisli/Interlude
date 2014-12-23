package game.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import game.buttons.Button;
import game.pop_ups.PopUp;
import music.Instrument;
import music.Music;
import music.Voice;

import org.newdawn.slick.Graphics;

public class InstrumentSelectionPage implements Scene {
    private final List<Button> buttons = new ArrayList<Button>();
    private final Music music;
    private Optional<Instrument> selectedInstrument;
    
    public InstrumentSelectionPage( Music music ) {
        this.music = music;
    }
    
    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.songSelection();
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( Button button : buttons ) {
            button.render(g);
        }
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for ( Button button : buttons ) {
            button.update(t);
        }
    }

    @Override
    public void init() {
        List<Voice> voices = music.voices();
        int numVoices = voices.size();
        for ( int i = 0; i < numVoices; i++ ) {
            Voice voice = voices.get(i);
            buttons.add( Button.textButton( voice.instrument().getInstrumentName(), ((float) (i+1)) / (numVoices + 1), 0.5f, (Runnable) () -> {
                selectedInstrument = Optional.of(voice.instrument());
            }));
        }
        
        buttons.add( Button.textButton("OK!", 0.5f, 0.9f, (Runnable) () -> {
            SceneManager.setNewScene( Scene.round(music, selectedInstrument.get()) );
        }));
    }

}
