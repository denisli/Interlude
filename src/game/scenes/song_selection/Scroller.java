package game.scenes.song_selection;

import game.Renderable;
import game.Updateable;

import org.newdawn.slick.MouseListener;

public interface Scroller extends Renderable, Updateable, MouseListener {
    public static Scroller songSelectionScroller() {
        Scroller scroller = new SongSelectionScroller();
        scroller.init();
        return scroller;
    }
}
