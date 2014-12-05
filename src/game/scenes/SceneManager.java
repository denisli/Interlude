package game.scenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class SceneManager {
    private Scene currentScene;
    
    public SceneManager() {
        this.currentScene = Scene.mainMenu();//Scene.mainMenu();
    }
    
    public void render(GameContainer gc, Graphics g) {
        currentScene.render(gc, g);
    }
    
    public void update(GameContainer gc, int t) {
        if (currentScene != currentScene.nextScene(gc, t)) {
            currentScene = currentScene.nextScene(gc, t);
        } else {
            currentScene.update(gc, t);
        }
    }
    
    public void init(GameContainer gc) {
        currentScene.init(gc);
    }
}
