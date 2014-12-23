package game;

public interface Movable {
    public void moveLeft( float fractionX );
    
    public void moveRight( float fractionX );
    
    public void moveDown( float fractionY );
    
    public void moveUp( float fractionY );
}
