package game.buttons.effects;

import game.scenes.Scene;
import game.scenes.SceneManager;

public class ChangeSceneEffect implements Runnable {
    private Scene scene;
    
    public ChangeSceneEffect( Scene scene ) {
        this.scene = scene;
    }
    
    @Override
    public void run() {
        // TODO Auto-generated method stub
        SceneManager.setNewScene(scene);
    }
    
}
