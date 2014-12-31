package music;

import java.util.Comparator;

import util.Quadruple;

public class OffTimesComparator implements Comparator<Quadruple<Long,Integer,Integer,Integer>>{

    @Override
    public int compare(Quadruple<Long, Integer, Integer, Integer> quad,
            Quadruple<Long, Integer, Integer, Integer> otherQuad) {
        return (int) (quad.getLeft() - otherQuad.getLeft());
    }
}
