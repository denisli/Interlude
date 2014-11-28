package game;

import org.newdawn.slick.Input;

public class Controls {
    private int ANoteKey = Input.KEY_A;
    private int BNoteKey = Input.KEY_S; 
    private int CNoteKey = Input.KEY_D;
    private int DNoteKey = Input.KEY_F;
    private int ENoteKey = Input.KEY_J;
    private int FNoteKey = Input.KEY_K;
    private int GNoteKey = Input.KEY_L;
    
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
