package game;

import org.newdawn.slick.Input;

public interface Hoverable extends Renderable, Updateable {
	public boolean isHovered(Input input);
	
	public void hover(Input input);
}
