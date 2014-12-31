package util;

public class Quadruple<T1,T2,T3,T4> {
    private final T1 left;
    private final T2 middleLeft;
    private final T3 middleRight;
    private final T4 right;
    
    public Quadruple( T1 left, T2 middleLeft, T3 middleRight, T4 right ) {
        this.left = left;
        this.middleLeft = middleLeft;
        this.middleRight = middleRight;
        this.right = right;
    }
    
    public T1 getLeft() {
        return left;
    }
    
    public T2 getMiddleLeft() {
        return middleLeft;
    }
    
    public T3 getMiddleRight() {
        return middleRight;
    }
    
    public T4 getRight() {
        return right;
    }
}
