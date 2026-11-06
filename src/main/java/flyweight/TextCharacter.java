
package flyweight;

import java.util.Objects;

public class TextCharacter {

    private char symbol;
    private CharacterStyle style;

    public TextCharacter(char symbol, CharacterStyle style) {
        this.symbol = symbol;
        this.style = Objects.requireNonNull(style);
    }

    public char getSymbol() {
        return symbol;
    }

    public CharacterStyle getStyle() {
        return style;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void setStyle(CharacterStyle style) {
        this.style = Objects.requireNonNull(style);
    }
}
