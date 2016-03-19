package game;
import org.newdawn.slick.Graphics;


public interface GameObject {

	public void init();
	
	public void update(int dt);
	
	public void render(Graphics g);
	
}
