package game.scenes;

import game.buttons.Button;
import game.fonts.GameFonts;
import game.labels.Label;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ScorePage extends Scene {
    private final Label titleLabel;
    private final Label scoreLabel;
    private final Button mainMenuButton;
    
    public ScorePage( String musicTitle, int score ) {
        this.titleLabel = Label.label(musicTitle, 0.5f, 0.3f, Color.black, GameFonts.ARIAL_PLAIN_54);
        this.scoreLabel = Label.label(Integer.toString(score), 0.5f, 0.5f, Color.black, GameFonts.ARIAL_PLAIN_54);
        this.mainMenuButton = Button.mainMenuButton( 0.8f, 0.05f );
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
        throw new RuntimeException("Score page should have no parent scene to go back to");
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        titleLabel.render(g);
        scoreLabel.render(g);
        mainMenuButton.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        titleLabel.update(t);
        scoreLabel.update(t);
        mainMenuButton.update(t);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }
}
