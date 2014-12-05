package music;

import java.util.List;

public class Simultaneous implements SoundElement {
    public static final int S = 128;
    
    private final List<Note> notes;
    
    public Simultaneous(List<Note> notes) {
        this.notes = notes;
    }
    
    // not the actual duration. Maybe I should change it to say minDuration
    @Override
    public int duration() {
        int minDuration = notes.get(0).duration();
        for (Note note : notes) {
            minDuration = Math.min(minDuration, note.duration());
        }
        return minDuration;
    }

    public List<Note> notes() {
        return notes;
    }

    @Override
    public int letter() {
        return Simultaneous.S;
    }

    @Override
    public SoundElement correspondingSoundElement(int letter) {
        // TODO Auto-generated method stub
        if (letter == Simultaneous.S) {
            return this;
        }
        return notes.get(0).correspondingSoundElement(letter);
    }
}
