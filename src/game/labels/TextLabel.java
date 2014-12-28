package game.labels;

import game.Interlude;

import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

public class TextLabel<T> implements Label<T> {
    private T object;
    private String text;
    private float fractionX;
    private float fractionY;
    private final Color color;
    private final UnicodeFont font;
    private final Function<T,String> stringer;
    
    public TextLabel( T object, float fractionX, float fractionY, Color color, UnicodeFont font, Function<T,String> stringer ) {
        this.object = object;
        this.text = stringer.apply(object);
        this.fractionX = fractionX;
        this.fractionY = fractionY;
        this.color = color;
        this.font = font;
        this.stringer = stringer;
    }

    @Override
    public void render(Graphics g) {
        // TODO Auto-generated method stub
        int containerWidth = Interlude.GAME_CONTAINER.getWidth();
        int containerHeight = Interlude.GAME_CONTAINER.getHeight();
        int textWidth = font.getWidth(text);
        int textHeight = font.getHeight(text);
        g.setFont(font);
        g.setColor(color);
        g.drawString( text, fractionX * containerWidth - textWidth / 2, fractionY * containerHeight - textHeight / 2 );
    }

    @Override
    public void update(int t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateValue(T newValue) {
        // TODO Auto-generated method stub
        this.object = newValue;
        this.text = stringer.apply(newValue);
    }

    @Override
    public T getValue() {
        // TODO Auto-generated method stub
        return object;
    }
}
