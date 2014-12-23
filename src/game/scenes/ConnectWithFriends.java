package game.scenes;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.SimpleFont;
import game.buttons.Button;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class ConnectWithFriends implements Scene {
    TextField connectionIDTextField = 
            new TextField( Interlude.GAME_CONTAINER.context(), 
                           SimpleFont.retrieve("Arial",Font.PLAIN,36),
                           300, 300, 400, 50);
    List<Button> buttons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f,0.1f))); 
    
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
        return Scene.mainMenu();
    }

    @Override
    public String name() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        for ( Button button : buttons ) {
            button.render(g);
        }
        connectionIDTextField.setCursorVisible(true);
        connectionIDTextField.setBorderColor( Color.red );
        g.setColor( Color.lightGray );
        connectionIDTextField.setBackgroundColor( Color.lightGray );
        connectionIDTextField.setTextColor( Color.black );
        connectionIDTextField.render(Interlude.GAME_CONTAINER.context(), g);
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        for ( Button button : buttons ) {
            button.update(t);
        }
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

}
