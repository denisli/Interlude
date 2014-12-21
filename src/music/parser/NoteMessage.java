package music.parser;

public class NoteMessage implements Comparable<NoteMessage> {
    private final long tick;
    private final int value;
    private final double millisecondsPerTick;
    private final int volume;
    private final boolean on;
    
    public NoteMessage( long tick, int value, double millisecondsPerTick, int volume, boolean on ) {
        this.tick = tick;
        this.value = value;
        this.millisecondsPerTick = millisecondsPerTick;
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
    
    public double millisecondsPerTick() {
        return millisecondsPerTick;
    }
    
    public int volume() {
        return volume;
    }
    
    public boolean on() {
        return on;
    }
}