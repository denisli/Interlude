package game;

import music.Note;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class MovingNote {
    private static double speed;
    private int note;
    private double x;
    private double y;
    
    public void render(GameContainer gc, Graphics g) {
        switch (note) {
        case Note.A: return;
        case Note.AS: return;
        case Note.B: return;
        case Note.C: return;
        case Note.CS: return;
        case Note.D: return;
        case Note.DS: return;
        case Note.E: return;
        case Note.F: return;
        case Note.FS: return;
        case Note.G: return;
        case Note.GS: return;
        }
    }
    
    public void update(GameContainer gc, int t) {
        x -= speed * t;
    }
}
