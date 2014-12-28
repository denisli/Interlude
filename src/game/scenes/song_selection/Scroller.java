package game.scenes.song_selection;

import game.Renderable;

import org.newdawn.slick.MouseListener;

public interface Scroller extends Renderable, MouseListener {
    public static Scroller songSelectionScroller() {
        Scroller scroller = new SongSelectionScroller();
        scroller.init();
        return scroller;
    }
}
