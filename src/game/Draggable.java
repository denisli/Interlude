package game;

import org.newdawn.slick.Input;

public interface Draggable extends Moveable {
	public boolean isDragged(Input input);
	
	public void drag(Input input);
}
