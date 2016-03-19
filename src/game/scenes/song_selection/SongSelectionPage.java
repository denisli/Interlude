package game.scenes.song_selection;

import java.util.ArrayList;
import java.util.List;

import game.Interlude;
import game.GameObject;
import game.buttons.Button;
import game.scenes.Scene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class SongSelectionPage extends Scene {
    private final Scroller songSelectionScroller = Scroller.songSelectionScroller();
    private List<GameObject> renderables = new ArrayList<GameObject>();

    @Override
    public void render(Graphics g) {
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public void init() {
        layout();
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        input.addMouseListener( songSelectionScroller );
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        Input input = Interlude.GAME_CONTAINER.getInput();
        input.removeMouseListener( songSelectionScroller );
    }

    @Override
    protected void layout() {
        // layout buttons
        Button backButton = Button.backButton( 0.9f, 0.05f );
        renderables.add(backButton);
        // add scroller
        renderables.add(songSelectionScroller);
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }
}
