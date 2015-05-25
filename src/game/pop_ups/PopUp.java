package game.pop_ups;

import game.Renderable;
import game.Updateable;
import game.scenes.Scene;

/**
 * PopUp represents a pop up in scene, but was not part of the scene. In other words, it is a large element 
 * that shows up on the scene due to some stimulus.
 */
public abstract class PopUp implements Renderable, Updateable {

    public void remove(Scene scene) {
        scene.destroyPopUp( this );
    }
    
    public void addOn(Scene scene) {
        scene.addPopUp( this );
    }
}
