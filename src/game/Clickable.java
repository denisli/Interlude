package game;

import org.newdawn.slick.Input;

public interface Clickable extends Renderable, Updateable {
	public boolean isClicked(Input input);
	
	public void click(Input input);
}
