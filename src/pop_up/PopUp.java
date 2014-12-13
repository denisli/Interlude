package pop_up;

import org.newdawn.slick.Graphics;

/**
 * PopUp represents a pop up in scene, but was not part of the scene. In other words, it is a large element 
 * that shows up on the scene due to some stimulus.
 */
public interface PopUp {
    public void addOn(Graphics g);
    
    public static void songEnded(Graphics g) {
        PopUp songEnded = new SongEnded();
        songEnded.addOn(g);
    }
}
