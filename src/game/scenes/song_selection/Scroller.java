package game.scenes.song_selection;

import game.GameObject;

import org.newdawn.slick.MouseListener;

public interface Scroller extends GameObject, MouseListener {
    public static Scroller songSelectionScroller() {
        Scroller scroller = SongSelectionScroller.getSongSelectionScroller();
        scroller.init();
        return scroller;
    }
}
