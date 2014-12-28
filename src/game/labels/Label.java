package game.labels;

import java.util.function.Function;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;

import game.Renderable;

public interface Label<T> extends Renderable {
    
    public static Label<String> textLabel(String text, float fractionX, float fractionY, Color color, UnicodeFont font) {
        Function<String,String> stringer = (Function<String,String>) ( string -> string );
        Label<String> label = new TextLabel<String>( text, fractionX, fractionY, color, font, stringer );
        label.init();
        return label;
    }
    
    public static <T> Label<T> textLabel(T object, float fractionX, float fractionY, Color color, UnicodeFont font, Function<T,String> stringer) {
        Label<T> label = new TextLabel<T>( object, fractionX, fractionY, color, font, stringer );
        label.init();
        return label;
    }
    
    public T getValue();
    
    public void updateValue(T newValue);
}
