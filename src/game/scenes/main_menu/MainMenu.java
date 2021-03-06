package game.scenes.main_menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.GameObject;
import game.buttons.Button;
import game.buttons.effects.ChangeSceneEffect;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class MainMenu extends Scene {
    private final List<GameObject> renderables = new ArrayList<GameObject>();
    
    @Override
    public void render(Graphics g) {
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public Scene parentScene() {
        throw new RuntimeException("Main menu has no parent scene!");
    }

    @Override
    public void cleanUp() {
        
    }

    @Override
    protected void layout() {
        // indicate buttons on page and, lastly, add them to the list of renderables the page has
        float buttonsCenterX = 0.5f;
        float buttonsInitialFractionY = 0.4f;
        float increment = 0.1f;
        Button playButton = Button.textButton( "Play", buttonsCenterX, buttonsInitialFractionY, new ChangeSceneEffect(Scene.songSelection()) );
        //Button playWithFriendsButton = Button.textButton( "Play with friends!", buttonsCenterX, buttonsInitialFractionY + increment, new ChangeSceneEffect(Scene.connectWithFriends()) );
        Button instructionsButton = Button.textButton( "Instructions", buttonsCenterX, buttonsInitialFractionY + increment, new ChangeSceneEffect(Scene.instructionsPage()) );
        Button controlsButton = Button.textButton( "Controls", buttonsCenterX, buttonsInitialFractionY + 2 * increment, new ChangeSceneEffect(Scene.changeControlsPage()) );
        Button optionsButton = Button.textButton( "Options", buttonsCenterX, buttonsInitialFractionY + 3 * increment, new ChangeSceneEffect(Scene.optionsPage()) );
        renderables.addAll( Arrays.asList(playButton, /*playWithFriendsButton,*/ instructionsButton, controlsButton, optionsButton) );
        
        // indicate labels on page and, lastly, add them to the list of renderables on page
        float interludeLabelX = 0.5f;
        float interludeLabelY = 0.15f;
        Label<String> interludeLabel = Label.textLabel( "Interlude", interludeLabelX, interludeLabelY, Color.gray, GameFonts.ARIAL_PLAIN_54 );
        renderables.add(interludeLabel);
    }

}
