package game.selectables;

import org.newdawn.slick.Input;

import game.GameObject;
import game.Interlude;

public abstract class Selectable implements GameObject {
    private boolean selected = false;
    
    private Statement condition;
    private Runnable effect;
    
    public Selectable( Statement condition, Runnable effect ) {
        this.condition = condition;
        this.effect = effect;
    }
    
    public static Selectable textSelectable( String text, float fractionX, float fractionY, Statement condition, Runnable effect ) {
        Selectable selectable = new TextSelectable( text, fractionX, fractionY, condition, effect );
        selectable.init();
        return selectable;
    }
    
    @Override
    public void update(int t) {
        checkCondition();
        
        Input input = Interlude.GAME_CONTAINER.getInput();
        if ( isClicked( input ) ) {
            callEffect();
        }
    }

    private void checkCondition() {
        if ( condition.isTrue() ) {
            selected = true;
        } else {
            selected = false;
        }
    }
    
    protected abstract boolean isClicked(Input input);
    
    public boolean selected() {
        return selected;
    }
    
    protected void callEffect() {
        effect.run();
    }
}
