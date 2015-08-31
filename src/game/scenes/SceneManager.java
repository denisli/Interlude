package game.scenes;

import game.Interlude;
import game.scenes.initialization_scene.InitializationScene;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SceneManager {
    private static Scene currentScene = Scene.initializationScene();
    private static Image background;
    static {
    	try {
    		int containerWidth = Interlude.GAME_CONTAINER.getWidth();
    		int containerHeight = Interlude.GAME_CONTAINER.getHeight();
			background = new Image("images/musical_background.png");
			background = background.getScaledCopy(containerWidth, containerHeight);
		} catch (SlickException e) {
			e.printStackTrace();
		}
    }
    
    public static void render(Graphics g) {
    	if ( !(currentScene instanceof InitializationScene) ) {
    		background.draw(0,0);
    	}
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
