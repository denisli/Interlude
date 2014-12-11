package game;

import music.Note;
import music.Simultaneous;

import org.newdawn.slick.Input;

public class Controls {
    private int ANoteKey = Input.KEY_A;
    private int BNoteKey = Input.KEY_S; 
    private int CNoteKey = Input.KEY_D;
    private int DNoteKey = Input.KEY_F;
    private int ENoteKey = Input.KEY_J;
    private int FNoteKey = Input.KEY_K;
    private int GNoteKey = Input.KEY_L;
    private int simultaneousKey = Input.KEY_SEMICOLON;
    
    private int[] noteKeys = new int[] { ANoteKey, BNoteKey, CNoteKey, DNoteKey, 
                                         ENoteKey, FNoteKey, GNoteKey, simultaneousKey };
    
    private int[] notes = new int[] { Note.A, Note.B, Note.C, Note.D, 
            Note.E, Note.F, Note.G, Simultaneous.S };
    
    public int[] noteKeys() {
        return noteKeys;
    }
    
    public boolean noteKeyPressed(Input input) {
        for ( int key : noteKeys ) {
            if ( input.isKeyPressed(key) ) {
                return true;
            }
        }
        return false;
    }
    
    public int correspondingNoteOfKey(int key) {
        if ( key == ANoteKey ) {
            return Note.A;
        } else if ( key == BNoteKey ) {
            return Note.B;
        } else if ( key == CNoteKey ) {
            return Note.C;
        } else if ( key == DNoteKey ) {
            return Note.D;
        } else if ( key == ENoteKey ) {
            return Note.E;
        } else if ( key == FNoteKey ) {
            return Note.F;
        } else if ( key == GNoteKey ) {
            return Note.G;
        } else if ( key == simultaneousKey ) {
            return Simultaneous.S;
        } else {
            throw new IllegalArgumentException("Key given is not a note key");
        }
    }
    
    public int getANoteKey() {
        return ANoteKey;
    }

    public void setANoteKey(int aNoteKey) {
        ANoteKey = aNoteKey;
    }

    public int getBNoteKey() {
        return BNoteKey;
    }

    public void setBNoteKey(int bNoteKey) {
        BNoteKey = bNoteKey;
    }

    public int getCNoteKey() {
        return CNoteKey;
    }

    public void setCNoteKey(int cNoteKey) {
        CNoteKey = cNoteKey;
    }

    public int getDNoteKey() {
        return DNoteKey;
    }

    public void setDNoteKey(int dNoteKey) {
        DNoteKey = dNoteKey;
    }

    public int getENoteKey() {
        return ENoteKey;
    }

    public void setENoteKey(int eNoteKey) {
        ENoteKey = eNoteKey;
    }

    public int getFNoteKey() {
        return FNoteKey;
    }

    public void setFNoteKey(int fNoteKey) {
        FNoteKey = fNoteKey;
    }

    public int getGNoteKey() {
        return GNoteKey;
    }

    public void setGNoteKey(int gNoteKey) {
        GNoteKey = gNoteKey;
    }
    
    public int getSimultaneousKey() {
        return simultaneousKey;
    }
    
    public void setSimultaneousKey(int simultaneousKey) {
        this.simultaneousKey = simultaneousKey;
    }
    
    public void resetKeys() {
        ANoteKey = Input.KEY_A;
        BNoteKey = Input.KEY_S; 
        CNoteKey = Input.KEY_D;
        DNoteKey = Input.KEY_F;
        ENoteKey = Input.KEY_J;
        FNoteKey = Input.KEY_K;
        GNoteKey = Input.KEY_L;
    }
}
