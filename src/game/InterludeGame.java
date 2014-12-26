package game;

import game.scenes.SceneManager;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class InterludeGame extends BasicGame { 
    private static GameContainer container;
    
    public InterludeGame() {
        super("Interlude");
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setBackground( Color.white );
        SceneManager.render(g);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        container = gc;
        SceneManager.init();
    }

    @Override
    public void update(GameContainer gc, int t) throws SlickException {
        SceneManager.update(t);
    }
    
    public static GameContainer gameContainer() {
        return container;
    }
}
