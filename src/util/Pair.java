package util;

public class Pair<T1,T2> {
    private final T1 left;
    private final T2 right;
    
    public Pair(T1 left, T2 right) {
        this.left = left;
        this.right = right;
    }
    
    public T1 getLeft() {
        return left;
    }
    
    public T2 getRight() {
        return right;
    }
    
    @Override
    public boolean equals(Object other) {
        if ( ! (other instanceof Pair) ) {
            return false;
        } else {
            Pair otherPair = (Pair) other;
            return this.left.equals(otherPair.left) && this.right.equals(otherPair.right);
        }
    }
    
    @Override
    public int hashCode() {
        return left.hashCode() + right.hashCode();
    }
}
