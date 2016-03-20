package game.scenes.options;

import java.util.ArrayList;
import java.util.List;

import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;
import game.selectables.Selectable;
import game.selectables.Statement;
import game.settings.GameplayType;
import game.settings.GameplayTypeSetting;
import game.GameObject;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class OptionsPage extends Scene {
    List<GameObject> renderables = new ArrayList<GameObject>();
    
    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }

    @Override
    public void render(Graphics g) {
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        float fractionX = 0.5f;
        // labels
        renderables.add( Label.textLabel( "Options", fractionX, 0.2f, Color.darkGray, GameFonts.ARIAL_PLAIN_36 ) );

        // buttons
        renderables.add( Button.backButton( 0.9f, 0.1f ) );
        
        // selectables
        renderables.add( Selectable.textSelectable("ONLY ONE-HANDED", fractionX, 1.0f/3, new Statement() {
            public boolean isTrue() {
                return GameplayTypeSetting.gameplayType() == GameplayType.ONE_HANDED;
            }
        }, (Runnable) () -> GameplayTypeSetting.setGameplayType(GameplayType.ONE_HANDED) ));
        
        renderables.add( Selectable.textSelectable("ENABLED TWO-HANDED", fractionX, 2.0f/3, new Statement() {
            public boolean isTrue() {
                return GameplayTypeSetting.gameplayType() == GameplayType.TWO_HANDED;
            }
        }, (Runnable) () -> GameplayTypeSetting.setGameplayType(GameplayType.TWO_HANDED) ));
    }

}
