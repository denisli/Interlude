package game.scenes.round;

import java.util.ArrayList;
import java.util.List;

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

public class SpeedUpSlider implements Renderable, Updateable {
    private final float fractionX;
    private final float fractionY;
    private final TimeDilator timeDilator;
    
    private float stableButtonSliderFractionX;
    
    private static final double[] SPEED_UPS = { 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0 };
    private static final int NUM_POSSIBILITIES = SPEED_UPS.length;
    
    private static final String LEFT_TEXT = "0.5x SPEED";
    private static final String RIGHT_TEXT = "2x SPEED";
    private static final double RIGHT_VALUE = SPEED_UPS[NUM_POSSIBILITIES-1];
    private static final double LEFT_VALUE = SPEED_UPS[0];
    public static final float FRACTION_LENGTH = 0.8f;

    private boolean mouseWasDown = false;
    private boolean mouseWasPressed = false;
    private RoundedRectangle sliderButton;
    
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    private Label<String> leftLabel;
    private Label<String> rightLabel;
    
    public static SpeedUpSlider speedUpSlider( float fractionX, float fractionY, TimeDilator timeDilator ) {
        SpeedUpSlider speedUpSlider = new SpeedUpSlider( fractionX, fractionY, timeDilator );
        speedUpSlider.init();
        return speedUpSlider;
    }
    
    public SpeedUpSlider( float fractionX, float fractionY, TimeDilator timeDilator ) {
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.timeDilator = timeDilator;
    }

    @Override
    public void render(Graphics g) {
        renderables.stream().forEach( renderable -> renderable.render(g) );
        
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int sliderY = (int) ( containerHeight * fractionY );
        int sliderLeft = (int) ( containerWidth * ( fractionX + FRACTION_LENGTH / 2 ) ); 
        int sliderRight = (int) ( containerWidth * ( fractionX - FRACTION_LENGTH / 2 ) ); 
        
        g.setColor( Color.gray );
        g.drawLine( sliderLeft, sliderY, sliderRight, sliderY);
        g.setColor( Color.blue );
        g.fill(sliderButton);
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> ((Updateable) renderable).update(t) );
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        
        int mouseX = input.getMouseX();
        int mouseY = input.getMouseY();
        int sliderLeft = (int) ( containerWidth * ( fractionX - FRACTION_LENGTH / 2 ) ); 
        int sliderRight = (int) ( containerWidth * ( fractionX + FRACTION_LENGTH / 2 ) ); 
        
        if ( input.isMouseButtonDown( Input.MOUSE_LEFT_BUTTON ) ) {
            if ( !mouseWasDown ) {
                if ( sliderButton.contains( mouseX, mouseY )) {
                    mouseWasPressed = true;
                }
            }
            
            if ( mouseWasPressed ) {
                int sliderButtonCenterX = Math.min(Math.max(sliderLeft,mouseX),sliderRight);
                sliderButton.setCenterX( sliderButtonCenterX );
                
                double sliderButtonFractionPosition = ((double) ( sliderButtonCenterX - sliderLeft )) / ( sliderRight - sliderLeft );
                double sliderButtonApproximateIndex = (NUM_POSSIBILITIES - 1) * sliderButtonFractionPosition;
                int closestIndex = (int) Math.round(sliderButtonApproximateIndex);
                double distFromClosestIndex = Math.abs(sliderButtonApproximateIndex - closestIndex);
                double threshold = 0.25;
                if ( distFromClosestIndex < threshold ) {
                	stableButtonSliderFractionX = ((float) closestIndex) / (NUM_POSSIBILITIES-1);
                }
                
                timeDilator.timeDilationFactor = 1.0/SPEED_UPS[closestIndex];
            }
            
            mouseWasDown = true;
        } else {
        	sliderButton.setCenterX(sliderLeft + (sliderRight - sliderLeft) * stableButtonSliderFractionX);
            mouseWasDown = false;
            mouseWasPressed = false;
        }
        
    }

    @Override
    public void init() {
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        
        int sliderY = (int) ( containerHeight * fractionY );
        int sliderButtonWidth = 40;
        int sliderButtonHeight = 20;
        int cornerRadius = 6;
        int sliderLeft = (int) ( containerWidth * ( fractionX - FRACTION_LENGTH / 2 ) ); 
        int sliderRight = (int) ( containerWidth * ( fractionX + FRACTION_LENGTH / 2 ) ); 
        double speedUp = 1.0 / timeDilator.timeDilationFactor;
        double valueFractionPosition = ( speedUp - LEFT_VALUE ) / ( RIGHT_VALUE - LEFT_VALUE );
        int sliderButtonCenterX = (int) (sliderLeft + ( sliderRight - sliderLeft ) * valueFractionPosition);
        stableButtonSliderFractionX = (float) valueFractionPosition;
        
        sliderButton = new RoundedRectangle( sliderButtonCenterX - sliderButtonWidth / 2, sliderY - sliderButtonHeight / 2, sliderButtonWidth, sliderButtonHeight, cornerRadius );
        UnicodeFont labelFont = GameFonts.ARIAL_PLAIN_14;
        leftLabel = Label.textLabel(LEFT_TEXT, fractionX - (FRACTION_LENGTH / 2 + 0.02f), fractionY, Color.black, labelFont);
        rightLabel = Label.textLabel(RIGHT_TEXT, fractionX + (FRACTION_LENGTH / 2 + 0.02f), fractionY, Color.black, labelFont);
        renderables.add(leftLabel);
        renderables.add(rightLabel);
    }

    public void reset() {
    	int containerWidth = Interlude.GAME_CONTAINER.getWidth();
    	stableButtonSliderFractionX = (float) ( (1.0 - LEFT_VALUE ) / ( RIGHT_VALUE - LEFT_VALUE ) );
    	int sliderLeft = (int) ( containerWidth * ( fractionX - FRACTION_LENGTH / 2 ) ); 
        int sliderRight = (int) ( containerWidth * ( fractionX + FRACTION_LENGTH / 2 ) ); 
        double valueFractionPosition = ( 1.0 - LEFT_VALUE ) / ( RIGHT_VALUE - LEFT_VALUE );
        int sliderButtonCenterX = (int) (sliderLeft + ( sliderRight - sliderLeft ) * valueFractionPosition);
        sliderButton.setCenterX( sliderButtonCenterX );
        timeDilator.timeDilationFactor = 1.0;
    }
}
