package game.scenes;

import java.awt.Font;

import game.Interlude;
import game.SimpleFont;
import game.pop_ups.PopUp;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class ConnectWithFriends implements Scene {
    TextField connectionIDTextField = 
            new TextField( Interlude.GAME_CONTAINER.context(), 
                           SimpleFont.retrieve("Arial",Font.PLAIN,36),
                           300, 300, 80, 80);
    
    @Override
    public void addPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void destroyPopUp(PopUp popUp) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Scene parentScene() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

}
