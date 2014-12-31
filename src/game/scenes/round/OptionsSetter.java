package game.scenes.round;

import game.Interlude;
import game.Renderable;
import game.buttons.Button;
import game.pop_ups.PopUp;
import game.scenes.SceneManager;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.RoundedRectangle;

import music.Instrument;

public class OptionsSetter extends PopUp {
    private final List<Instrument> instruments;
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private static final float SLIDER_FRACTION_Y = 0.5f;
    
    public static PopUp optionsSetter( List<Instrument> instruments ) {
        OptionsSetter optionsSetter = new OptionsSetter( instruments );
        optionsSetter.init();
        return optionsSetter;
    }
    
    public OptionsSetter( List<Instrument> instruments ) {
        this.instruments = instruments;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        float topFractionPadding = 0.1f;
        float bottomFractionPadding = 0.2f;
        float totalFractionPadding = topFractionPadding + bottomFractionPadding;
                
        final float sliderTopFractionY = SLIDER_FRACTION_Y - VolumeSlider.FRACTION_HEIGHT / 2; 
        RoundedRectangle mat = new RoundedRectangle( 0, (int) ( containerHeight * ( sliderTopFractionY - topFractionPadding ) ), containerWidth, (int) ( containerHeight * ( VolumeSlider.FRACTION_HEIGHT + totalFractionPadding ) ), 20 );
        g.setColor( Color.orange );
        g.fill(mat);
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        int numInstruments = instruments.size();
        for ( int i = 0; i < numInstruments; i++ ) {
            Instrument instrument = instruments.get(i);
            renderables.add( VolumeSlider.volumeSlider(instrument, ((float) i+1) / (numInstruments + 1), SLIDER_FRACTION_Y));
        }
        
        Button okayButton = Button.textButton( "OK", 0.5f, 0.7f, (Runnable) () -> {
            this.remove( SceneManager.currentScene() );
        });
        Button resetButton = Button.textButton( "Reset", 0.75f, 0.7f, (Runnable) () -> {
            for ( Renderable renderable : renderables ) {
                if ( renderable instanceof VolumeSlider ) {
                    VolumeSlider volumeSlider = (VolumeSlider) renderable;
                    volumeSlider.reset();
                }
            }
        });
        renderables.add(okayButton);
        renderables.add(resetButton);
    }
}
