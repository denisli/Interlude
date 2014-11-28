package game.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class SceneManager {
    private Scene currentScene = Scene.mainMenu();
    
    public void render(GameContainer gc, Graphics g) {
        currentScene.render(gc, g);
    }
    
    public void update(GameContainer gc, int t) {
        currentScene = currentScene.nextScene(gc, t);
        currentScene.update(gc, t);
    }
}
