package game.scenes;

import java.awt.Font;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Client;
import game.Interlude;
import game.buttons.Button;
import game.fonts.SimpleFont;
import game.pop_ups.PopUp;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class ConnectWithFriends extends Scene {
    
    TextField connectionIDTextField = 
            new TextField( Interlude.GAME_CONTAINER.context(), 
                           SimpleFont.retrieve("Arial",Font.PLAIN,36),
                           300, 300, 400, 50);
    List<Button> buttons = new ArrayList<Button>(Arrays.asList(Button.backButton(0.9f,0.1f),Button.startConnectionButton(0.5f, 0.3f))); 
    
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
        Button button = Button.textButton("Connect to server", 0.5f, 0.5f, (Runnable) () ->  { });
        button.setEffect( (Runnable) () -> {
            if ( button.isClicked( Interlude.GAME_CONTAINER.getInput() ) ) {
                if ( connectionIDTextField.getText().equals( "4444" ) ) {
                    try {
                        Client.connectToServer( "localhost", 4888 );
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        buttons.add(button);
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

}
