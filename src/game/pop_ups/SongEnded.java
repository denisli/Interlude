package game.pop_ups;

import game.Interlude;
import game.scenes.Scene;

import org.newdawn.slick.Graphics;

public class SongEnded implements PopUp {

    @Override
    public void addOn(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        g.drawString("Congratulations! You have completed this. Yay!, blah, blah", containerWidth / 2, containerHeight / 2);
    }
}
