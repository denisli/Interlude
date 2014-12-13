package game;

import org.newdawn.slick.Graphics;

public interface Renderable {
    public void render(Graphics g);
    
    public void update(int t);
    
    public void init();
}
