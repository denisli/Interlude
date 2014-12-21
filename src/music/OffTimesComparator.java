package music;

import java.util.Comparator;

import util.Triple;


public class OffTimesComparator implements Comparator<Triple<Long,Integer,Integer>>{

    @Override
    public int compare(Triple<Long, Integer, Integer> triple,
            Triple<Long, Integer, Integer> otherTriple) {
        return (int) (triple.getLeft() - otherTriple.getLeft());
    }
}
