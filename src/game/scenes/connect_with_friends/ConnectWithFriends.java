package game.scenes.connect_with_friends;

import java.awt.Font;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Interlude;
import game.Renderable;
import game.buttons.Button;
import game.fonts.GameFonts;
import game.fonts.SimpleFont;
import game.labels.Label;
import game.pop_ups.PopUp;
import game.scenes.Scene;
import game.server_client.Client;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class ConnectWithFriends extends Scene {
    
    TextField connectionIDTextField = 
            new TextField( Interlude.GAME_CONTAINER.context(), 
                           SimpleFont.retrieve("Arial",Font.PLAIN,36),
                           300, 300, 400, 50);
    List<Renderable> renderables = new ArrayList<Renderable>(Arrays.asList(Button.backButton(0.9f,0.1f),Button.startServerButton(0.5f, 0.3f))); 
    
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
        renderables.stream().forEach( renderable -> renderable.render(g) );
        
        connectionIDTextField.setCursorVisible(true);
        connectionIDTextField.setBorderColor( Color.red );
        g.setColor( Color.lightGray );
        connectionIDTextField.setBackgroundColor( Color.lightGray );
        connectionIDTextField.setTextColor( Color.black );
        connectionIDTextField.render(Interlude.GAME_CONTAINER.context(), g);
    }

    @Override
    public void update(int t) {
        renderables.stream().forEach( renderable -> renderable.update(t) );
    }

    @Override
    public void init() {
        super.init();
        // TODO Auto-generated method stub
        Button button = Button.textButton("Connect to server", 0.5f, 0.5f, (Runnable) () ->  { });
        button.setEffect( (Runnable) () -> {
            	System.out.println("Clicked?");
                	System.out.println("equals?");
                    try {
                        Client.connectToServer( connectionIDTextField.getText(), 8888 );
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
        });
        renderables.add(button);
    }

    @Override
    public void cleanUp() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void layout() {
        // TODO Auto-generated method stub
        try {
            renderables.add(Label.textLabel(InetAddress.getLocalHost().toString(), 0.3f, 0.1f, Color.red, GameFonts.ARIAL_PLAIN_36 ));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleServerMessages() {
        // TODO Auto-generated method stub
        
    }

}
