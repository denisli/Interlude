package game;

import java.awt.Dimension;
import java.awt.Toolkit;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class InterludeGameContainer {
    private final InterludeGame interludeGame;
    private AppGameContainer app;
    
    public InterludeGameContainer( InterludeGame interludeGame ) {
        this.interludeGame = interludeGame;
    }
    
    public void init() throws SlickException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) Math.floor( screenSize.getWidth() );
        int height = (int) Math.floor( screenSize.getHeight() );
        app = new AppGameContainer( interludeGame );
        app.setDisplayMode( width, height, false );
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
    
    public void start() throws SlickException {
        app.start();
    }
}
