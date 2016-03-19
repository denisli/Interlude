package game.buttons;

import game.GameObject;
import game.Interlude;
import game.scenes.SceneManager;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Shape;

public abstract class Button implements GameObject {
	Shape boundingShape;
	Runnable effect;
	
	boolean mouseWasDown = false;
	boolean mouseIsDown = false;
	boolean mouseWasInBound = false;
	boolean mouseIsInBound = false;
	
	
    public static Button backButton(float fractionX, float fractionY) {
        Button button = new TextButton("Back", fractionX, fractionY, (Runnable) () -> {
            SceneManager.setNewScene(SceneManager.currentScene().parentScene());
        });
        button.init();
        return button;
    }
    
    public static Button textButton(String text, float fractionX, float fractionY, Runnable effect) {
        Button button = new TextButton( text, fractionX, fractionY, effect );
        button.init();
        return button;
    }
    
    public static Button twoFaceButton( String firstText, String secondText, float fractionX, float fractionY, Runnable firstEffect, Runnable secondEffect ) {
        Button button = new TwoFaceButton( firstText, secondText, fractionX, fractionY, firstEffect, secondEffect );
        button.init();
        return button;
    }
    
    @Override
    public void update(int t) {
    	Input input = Interlude.GAME_CONTAINER.getInput();
    	
    	mouseWasDown = mouseIsDown; // change mouse was down based on previous upate
    	mouseIsDown = input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON);
    	
    	int mouseX = input.getMouseX();
    	int mouseY = input.getMouseY();
    	mouseWasInBound = mouseIsInBound; // use result from previous iteration
    	mouseIsInBound = boundingShape.contains(mouseX, mouseY);
    	
    	if ( isHovered(input) ) {
    		hover(input);
    	} else if ( isClicking(input) ) {
    		clicking(input);
    	} else if ( isClicked(input) ) {
    		click(input);
    	} else { // none of these occur
    		normalState(input);
    	}
    }
    
    public boolean isHovered(Input input) {
    	return !mouseWasDown && !mouseIsDown && mouseIsInBound;
    }
    
    public boolean isClicking(Input input) {
    	return mouseWasDown && mouseIsDown && mouseWasInBound && mouseIsInBound;
    }
    
    public boolean isClicked(Input input) {
    	return mouseWasDown && !mouseIsDown && mouseWasInBound && mouseIsInBound;
    }
    
    public abstract void hover(Input input);
    
    public abstract void clicking(Input input);
    
    public void click(Input input) {
    	effect.run();
    }
    
    abstract void setEffect(Runnable effect);
    
    abstract void normalState(Input input);
}
