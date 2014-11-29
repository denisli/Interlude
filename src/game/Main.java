package game;

import game.buttons.SimpleFont;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;

import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] args) throws SlickException, IOException {
        System.setProperty("org.lwjgl.librarypath",
                new File(new File(System.getProperty("user.dir"),"native"),
                        LWJGLUtil.getPlatformName()).getAbsolutePath());
        //System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) Math.floor( screenSize.getWidth() );
        int height = (int) Math.floor( screenSize.getHeight() );
        
        Display.setResizable(true);
        Game game = new Game();
        AppGameContainer app = new AppGameContainer( game );
        app.setDisplayMode( width, height, false );
        app.start();
    }
}
