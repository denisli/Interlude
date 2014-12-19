package game.scenes;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class SceneManager {
    private static Scene currentScene = Scene.mainMenu();
    
//    public SceneManager() {
//        this.currentScene = Scene.mainMenu();//Scene.mainMenu();
//    }
    
    public static void render(Graphics g) {
        g.setBackground( Color.white );
        currentScene.render(g);
    }
    
    public static void update(int t) {
//        if (currentScene != currentScene.nextScene(gc, t)) {
//            currentScene = currentScene.nextScene(gc, t);
//        } else {
//            currentScene.update(gc, t);
//        }
        currentScene.update(t);
    }
    
    public static void init() {
        currentScene.init();
    }
    
    public static Scene currentScene() {
        return currentScene;
    }
    
    /**
     * Changes the current scene and also initializes the new scene.
     * @param gc
     * @param scene
     */
    public static void setNewScene(Scene scene) {
        currentScene = scene;
        currentScene.init();
    }
}
