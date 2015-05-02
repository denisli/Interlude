package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class InterludeGameContainer {
    
    private final InterludeGame interludeGame;
    private AppGameContainer app;
    
    public InterludeGameContainer( InterludeGame interludeGame ) {
        this.interludeGame = interludeGame;
    }
    
    public void init() throws SlickException {
        int containerWidth = 900;//(int) Math.floor(Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        int containerHeight = 900;//(int) Math.floor(Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        
        app = new AppGameContainer( interludeGame );
        app.setDisplayMode( containerWidth, containerHeight, false );
        app.setShowFPS(false);
        app.setTargetFrameRate(60);
    }
    
    public int getWidth() {
        return app.getWidth();
    }
    
    public int getHeight() {
        return app.getHeight();
    }
    
    public Input getInput() {
        return app.getInput();
    }
    
    public GUIContext context() {
        return app;
    }
    
    public void start() throws SlickException {
        app.start();
    }
}
