package music.parser;

public class ProgramChangeMessage {
    private final long tick;
    private final int programNumber;
    private final int channel;
    
    public ProgramChangeMessage( long tick, int programNumber, int channel ) {
        this.tick = tick;
        this.programNumber = programNumber;
        this.channel = channel;
    }
}
