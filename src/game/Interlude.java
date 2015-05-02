package game;

import java.io.IOException;

import org.newdawn.slick.SlickException;

public class Interlude {
    public static final InterludeGameContainer GAME_CONTAINER = new InterludeGameContainer(new InterludeGame());
    
    public static void main(String[] args) throws SlickException, IOException {
        GAME_CONTAINER.init();
        GAME_CONTAINER.start();
    }
}