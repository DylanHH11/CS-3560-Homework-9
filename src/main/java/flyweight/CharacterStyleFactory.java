
package flyweight;

import java.util.HashMap;
import java.util.Map;

public class CharacterStyleFactory {

    private record StyleKey(String font, int size, String color) {}

    private final Map<StyleKey, CharacterStyle> styles =
            new HashMap<>();

    public CharacterStyle getStyle(String font, int size, String color) {

        StyleKey key = new StyleKey(font, size, color);

        return styles.computeIfAbsent(
                key,
                k -> new CharacterStyle(
                        k.font(),
                        k.size(),
                        k.color()
                )
        );
    }

    public int getStyleCount() {
        return styles.size();
    }
}
