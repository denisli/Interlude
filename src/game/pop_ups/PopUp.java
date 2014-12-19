package game.pop_ups;

import game.Renderable;
import game.VoiceType;
import game.scenes.Scene;

/**
 * PopUp represents a pop up in scene, but was not part of the scene. In other words, it is a large element 
 * that shows up on the scene due to some stimulus.
 */
public abstract class PopUp implements Renderable {
    private final Scene scene;
    
    public PopUp( Scene scene ) {
        this.scene = scene;
    }
    
    public static PopUp changeControl( int note, VoiceType voiceType, Scene scene ) {
        PopUp popUp = new ChangeControlPopUp( note, voiceType, scene );
        popUp.init();
        return popUp;
    }
    
    public Scene scene() {
        return scene;
    }
    
    public void remove(Scene scene) {
        scene.destroyPopUp( this );
    }
    
    public void addOn(Scene scene) {
        scene.addPopUp( this );
    }
}
