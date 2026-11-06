
package flyweight;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextDocument {

    private final List<TextCharacter> characters = new ArrayList<>();
    private final CharacterStyleFactory styleFactory;

    public TextDocument(CharacterStyleFactory styleFactory) {
        this.styleFactory = Objects.requireNonNull(styleFactory);
    }

    public void append(char symbol, String font, int size, String color) {
        CharacterStyle style = styleFactory.getStyle(font, size, color);
        characters.add(new TextCharacter(symbol, style));
    }

    public void insert(int index, char symbol,
                       String font, int size, String color) {
        CharacterStyle style = styleFactory.getStyle(font, size, color);
        characters.add(index, new TextCharacter(symbol, style));
    }

    public void replace(int index, char symbol) {
        characters.get(index).setSymbol(symbol);
    }

    public void format(int index, String font, int size, String color) {
        TextCharacter character = characters.get(index);
        CharacterStyle style = styleFactory.getStyle(font, size, color);
        character.setStyle(style);
    }

    public void delete(int index) {
        characters.remove(index);
    }

    public TextCharacter getCharacter(int index) {
        return characters.get(index);
    }

    public int length() {
        return characters.size();
    }

    public String getText() {
        StringBuilder result = new StringBuilder();

        for (TextCharacter character : characters) {
            result.append(character.getSymbol());
        }

        return result.toString();
    }
}
