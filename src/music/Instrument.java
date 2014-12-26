package music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Instrument {
    public static final List<Integer> TWO_HANDED_INSTRUMENTS = new ArrayList<Integer>(Arrays.asList(0));
    
    public static Instrument piano() {
        return new Piano();
    }
    
    public static Instrument instrumentFromName(String instrumentName) {
        if (instrumentName.equals("piano")) {
            return new Piano();
        } else {
            return null;
        }
    }
    
    public void update(int t);
    
    public void play(Note note);
    
    public void play(Simultaneous simultaneous);
        
    public String getInstrumentName();
    
    public int getProgram();
    
    public InstrumentType type();
    
    public void clear();
    
    @Override
    public boolean equals(Object other);
}
