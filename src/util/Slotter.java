package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Slotter<T> {
    private final List<T> elements;
    private Optional<T> lifted = Optional.empty();
    private Optional<Integer> unfilled = Optional.empty();
    
    public Slotter( List<T> elements ) {
        this.elements = elements;
    }
    
    public void lift( int position ) {
        unfilled = Optional.of(position);
        lifted = Optional.of(elements.get(position));
    }
    
    public void drop() {
        unfilled = Optional.empty();
        lifted = Optional.empty();
    }
    
    public void push( int position ) {
        if ( position < unfilled.get() ) {
            pushRight( position );
        } else if ( position > unfilled.get() ) {
            pushLeft( position );
        }
    }
    
    private void pushLeft( int position ) {
        int unfilledPosition = unfilled.get();
        
        for ( int i = unfilledPosition+1; i <= position; i++ ) {
            elements.set( i-1, elements.get( i ) );
        }
        
        elements.set( position, lifted.get() );
        unfilled = Optional.of(position);
    }
    
    private void pushRight( int position ) {
        int unfilledPosition = unfilled.get();
        
        for ( int i = unfilledPosition - 1; i >= position; i-- ) {
            elements.set( i+1, elements.get( i ) );
        }
        
        elements.set( position, lifted.get() );
        unfilled = Optional.of(position);
    }
    
    public int liftedIndex() {
        return unfilled.get();
    }
    
    public T get( int i ) {
        return elements.get(i);
    }
    
    public List<T> elements() {
        return elements;
    }
    
    public static void main(String[] args) {
        List<Integer> elements = new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6));
        Slotter<Integer> slotter = new Slotter<Integer>(elements);
        slotter.lift( 5 );
        slotter.push( 1 );
        slotter.drop();
        System.out.println( slotter.elements() );
        slotter.lift( 3 );
        slotter.push( 6 );
        slotter.drop();
        System.out.println( slotter.elements() );
        slotter.lift( 0 );
        slotter.push( 2 );
        slotter.push( 1 );
        slotter.push( 5 );
        slotter.drop();
        System.out.println( slotter.elements );
    }
}
