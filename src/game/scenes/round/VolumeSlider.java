package game.scenes.round;

import java.util.ArrayList;
import java.util.List;

import music.Instrument;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.RoundedRectangle;

import game.Interlude;
import game.Renderable;
import game.Updateable;
import game.fonts.GameFonts;
import game.labels.Label;

public class VolumeSlider implements Renderable, Updateable {
    private final Instrument instrument;
    private final float fractionX;
    private final float fractionY;
    
    private static final String TOP_TEXT = "2x VOLUME";
    private static final String BOTTOM_TEXT = "MUTE";
    private static final double TOP_VALUE = 2;
    private static final double BOTTOM_VALUE = 0;
    public static final float FRACTION_HEIGHT = 0.25f;

    private boolean mouseWasDown = false;
    private boolean mouseWasPressed = false;
    private double value;
    private RoundedRectangle sliderButton;
    
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private Label<String> instrumentLabel;
    private Label<String> topLabel;
    private Label<String> bottomLabel;
    
    public static VolumeSlider volumeSlider( Instrument instrument, float fractionX, float fractionY ) {
        VolumeSlider volumeSlider = new VolumeSlider( instrument, fractionX, fractionY );
        volumeSlider.init();
        return volumeSlider;
    }
    
    public VolumeSlider( Instrument instrument, float fractionX, float fractionY ) {
        this.instrument = instrument;
        this.fractionX = fractionX;
        this.fractionY = fractionY;
    }

    @Override
    public void render(Graphics g) {
        renderables.stream().forEach( renderable -> renderable.render(g) );
        
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int sliderX = (int) ( containerWidth * fractionX );
        int sliderTop = (int) ( containerHeight * ( fractionY + FRACTION_HEIGHT / 2 ) ); 
        int sliderBottom = (int) ( containerHeight * ( fractionY - FRACTION_HEIGHT / 2 ) ); 
        
        g.setColor( Color.gray );
        g.drawLine( sliderX, sliderTop, sliderX, sliderBottom);
        g.setColor( Color.blue );
        g.fill(sliderButton);
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> ((Updateable) renderable).update(t) );
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        int sliderTop = (int) ( containerHeight * ( fractionY - FRACTION_HEIGHT / 2 ) ); 
        int sliderBottom = (int) ( containerHeight * ( fractionY + FRACTION_HEIGHT / 2 ) ); 
        
        if ( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) ) {
            if ( !mouseWasDown ) {
                if ( sliderButton.contains( mouseX, mouseY )) {
                    mouseWasPressed = true;
                }
            }
            
            if ( mouseWasPressed ) {
                int sliderButtonCenterY = Math.min(Math.max(sliderTop,mouseY),sliderBottom);
                sliderButton.setCenterY( sliderButtonCenterY );
                
                double sliderButtonFractionPosition = ((double) ( sliderBottom - sliderButtonCenterY )) / ( sliderBottom - sliderTop );
                double ratio = BOTTOM_VALUE + ( TOP_VALUE - BOTTOM_VALUE ) * sliderButtonFractionPosition;
                instrument.setVolumeRatio(ratio);
            }
            
            mouseWasDown = true;
        } else {
            mouseWasDown = false;
            mouseWasPressed = false;
        }
        
    }

    @Override
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int sliderX = (int) ( containerWidth * fractionX );
        int sliderButtonWidth = 40;
        int sliderButtonHeight = 20;
        int cornerRadius = 6;
        int sliderTop = (int) ( containerHeight * ( fractionY - FRACTION_HEIGHT / 2 ) ); 
        int sliderBottom = (int) ( containerHeight * ( fractionY + FRACTION_HEIGHT / 2 ) ); 
        value = 1.0;
        double valueFractionPosition = ( value - BOTTOM_VALUE ) / ( TOP_VALUE - BOTTOM_VALUE );
        int sliderButtonCenterY = (int) (sliderBottom + ( sliderTop - sliderBottom ) * valueFractionPosition);
        
        sliderButton = new RoundedRectangle( sliderX - sliderButtonWidth / 2, sliderButtonCenterY - sliderButtonHeight / 2, sliderButtonWidth, sliderButtonHeight, cornerRadius );
        UnicodeFont labelFont = GameFonts.ARIAL_PLAIN_14;
        instrumentLabel = Label.textLabel(instrument.getInstrumentName(),fractionX, fractionY - (FRACTION_HEIGHT / 2 + 0.05f), Color.black, labelFont);
        topLabel = Label.textLabel(TOP_TEXT, fractionX, fractionY - (FRACTION_HEIGHT / 2 + 0.02f), Color.black, labelFont);
        bottomLabel = Label.textLabel(BOTTOM_TEXT, fractionX, fractionY + (FRACTION_HEIGHT / 2 + 0.02f), Color.black, labelFont);
        renderables.add(instrumentLabel);
        renderables.add(topLabel);
        renderables.add(bottomLabel);
    }

    public void reset() {
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        sliderButton.setCenterY( fractionY * containerHeight );
        instrument.setVolumeRatio(1.0);
    }
}
