package util;

public class Triple<T1,T2,T3> {
    private final T1 left;
    private final T2 middle;
    private final T3 right;
    
    public Triple( T1 left, T2 middle, T3 right ) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }
    
    public T1 getLeft() {
        return this.left;
    }
    
    public T2 getMiddle() {
        return this.middle;
    }
    
    public T3 getRight() {
        return this.right;
    }
}
