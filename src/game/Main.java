package game;

import java.io.File;


import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class Main {
    public static void main(String[] args) {
        System.setProperty("org.lwjgl.librarypath",
                new File(new File(System.getProperty("user.dir"),"native"),
                        LWJGLUtil.getPlatformName()).getAbsolutePath());
        
        try {
            Display.setResizable(true);
            AppGameContainer app = new AppGameContainer(new Game() );
            app.setDisplayMode(800, 600, false);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
        
    }
}
