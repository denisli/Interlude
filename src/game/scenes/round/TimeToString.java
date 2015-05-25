package game.scenes.round;

import java.util.function.Function;

public class TimeToString implements Function<Integer,String> {
    @Override
    public String apply(Integer duration) {
        int timeInSeconds = duration / 1000;
        int minutes = timeInSeconds / 60;
        int remainingSeconds = timeInSeconds % 60;
        
        return minutes + ":" + String.format("%02d",remainingSeconds);
    }
}
