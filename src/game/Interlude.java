package game;

import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.SlickException;

public class Interlude {
    public static final InterludeGameContainer GAME_CONTAINER = new InterludeGameContainer(new InterludeGame());
    
    public static void main(String[] args) throws SlickException, IOException {
        System.setProperty("org.lwjgl.librarypath",
                new File(new File(System.getProperty("user.dir"),"native"),
                        LWJGLUtil.getPlatformName()).getAbsolutePath());
        //System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
        GAME_CONTAINER.init();
        GAME_CONTAINER.start();
    }
}