package game;

import org.newdawn.slick.Input;

public interface Clickingable extends Renderable, Updateable {
	public boolean isClicking(Input input);
	
	public void clicking(Input input);
}
