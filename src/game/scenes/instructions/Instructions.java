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
        String instructionText = "You are to hit the moving notes as close to the note markers as possible.\n\n" +
                                 "You can change the orientation of the game with left and right arrows.\n" + 
                                 "You may also switch between displaying the note or key by using the up and down arrows.\n\n" +
                                 "Keep in mind that an \"S note\" represents two or more notes played at the same time.";
        renderables.add(Label.textLabel( instructionText, 0.5f, 0.4f, Color.black, GameFonts.ARIAL_PLAIN_18 ));
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }
}
