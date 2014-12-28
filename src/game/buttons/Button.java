package game.buttons;

import game.Movable;
import game.Renderable;
import game.scenes.SceneManager;
import game.server_client.Server;

import org.newdawn.slick.Input;

public interface Button extends Renderable, Movable {

    public static Button backButton(float fractionX, float fractionY) {
        Button button = new TextButton("Back", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(SceneManager.currentScene().parentScene());
        });
        button.init();
        return button;
    }
    
    public static Button startServerButton(float fractionX, float fractionY) {
        Button button = new TextButton("Start Connection", fractionX, fractionY, (Runnable) () -> {
            try {
                Server.startServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        button.init();
        return button;
    }
    
    public static Button textButton(String text, float fractionX, float fractionY, Runnable effect) {
        Button button = new TextButton( text, fractionX, fractionY, effect );
        button.init();
        return button;
    }
    
    public void setEffect(Runnable effect);
    
    public void callEffect();
    
    public boolean isClicked(Input input);
    
    public int width();
    
    public int height();
}
