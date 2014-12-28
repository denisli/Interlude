package game.scenes;

import org.newdawn.slick.Graphics;

public class SceneManager {
    private static Scene currentScene = Scene.initializationScene();
    
//    public SceneManager() {
//        this.currentScene = Scene.mainMenu();//Scene.mainMenu();
//    }
    
    public static void render(Graphics g) {
        currentScene.render(g);
    }
    
    public static void update(int t) {
        currentScene.update(t);
    }
    
    public static void init() {
        //currentScene.init();
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
        currentScene.cleanUp();
        currentScene = scene;
    }
}
