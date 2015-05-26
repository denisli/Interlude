package game.scenes.results_page;

import game.buttons.Button;
import game.buttons.effects.ChangeSceneEffect;
import game.fonts.GameFonts;
import game.labels.Label;
import game.scenes.Scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class ResultsPage extends Scene {
    private final String musicTitle;
    private final int score;
    
    private Label<String> titleLabel;
    private Label<String> scoreLabel;
    private Button backButton;
    private Button mainMenuButton;
    
    public ResultsPage( String musicTitle, int score ) {
        this.musicTitle = musicTitle;
        this.score = score;
    }

    @Override
    public Scene parentScene() {
    	return Scene.songSelection();
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        titleLabel.render(g);
        scoreLabel.render(g);
        mainMenuButton.render(g);
        backButton.render(g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        titleLabel.update(t);
        scoreLabel.update(t);
        mainMenuButton.update(t);
        backButton.update(t);
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        // TODO Auto-generated method stub
        this.titleLabel = Label.textLabel(musicTitle, 0.5f, 0.3f, Color.black, GameFonts.ARIAL_PLAIN_54);
        this.scoreLabel = Label.textLabel(Integer.toString(score), 0.5f, 0.5f, Color.black, GameFonts.ARIAL_PLAIN_54);
        this.mainMenuButton = Button.textButton( "Main Menu", 0.75f, 0.05f, new ChangeSceneEffect(Scene.mainMenu()) );
        this.backButton = Button.backButton(0.9f, 0.05f);
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }
}
