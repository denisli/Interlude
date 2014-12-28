package game.scenes.instructions;

import java.util.ArrayList;
import java.util.List;

import game.Renderable;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Instructions extends Scene {
    private final List<Renderable> renderables = new ArrayList<Renderable>();
    
    @Override
    public void render(Graphics g) {
        g.setColor(Color.yellow);
        g.drawString("Your goal is to hit the notes as close as possible to the circle at the end. Have fun!", 400, 400);
        renderables.stream().forEach( renderable -> renderable.render(g) );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return Scene.mainMenu();
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        // add in buttons
        renderables.add(Button.backButton( 0.9f, 0.1f ));
        
        // add in labels
        renderables.add(Label.textLabel( "Interlude", 0.5f, 0.15f, Color.gray, GameFonts.ARIAL_PLAIN_54 ));
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }
}
