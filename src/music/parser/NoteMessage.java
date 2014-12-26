package music.parser;

public class NoteMessage implements Comparable<NoteMessage> {
    private final long tick;
    private final int value;
    private final int volume;
    private final boolean on;
    
    public NoteMessage( long tick, int value, int volume, boolean on ) {
        this.tick = tick;
        this.value = value;
        this.volume = volume;
        this.on = on;
    }

    @Override
    public int compareTo(NoteMessage other) {
        // TODO Auto-generated method stub
        return (int) (this.tick - other.tick);
    }
    
    public long tick() {
        return tick;
    }
    
    public int value() {
        return value;
    }
    
    public int volume() {
        return volume;
    }
    
    public boolean on() {
        return on;
    }
}