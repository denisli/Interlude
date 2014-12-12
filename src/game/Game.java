package game;

import game.scenes.SceneManager;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame { 
    private final SceneManager sceneManager;
    
    public Game() {
        super("Interlude");
        this.sceneManager = new SceneManager();
    }
    
    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        // TODO Auto-generated method stub
        sceneManager.render(gc, g);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        // TODO Auto-generated method stub
        sceneManager.init(gc);
    }

    @Override
    public void update(GameContainer gc, int t) throws SlickException {
        // TODO Auto-generated method stub
        this.sceneManager.update(gc, t);
    }
}
