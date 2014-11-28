package music;

public interface Instrument {
    public static Instrument piano() {
        return new Piano();
    }
    
    public void playNote(Note note);
}
