
package flyweight;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterStyleFactoryTest {

    @Test
    public void testStyleProperties() {
        CharacterStyle style =
                new CharacterStyle("Arial", 12, "Red");

        assertAll(
                () -> assertEquals("Arial", style.getFont()),
                () -> assertEquals(12, style.getSize()),
                () -> assertEquals("Red", style.getColor())
        );
    }

    @Test
    public void testSameStyleIsShared() {
        CharacterStyleFactory factory =
                new CharacterStyleFactory();

        CharacterStyle first =
                factory.getStyle("Arial", 12, "Red");

        CharacterStyle second =
                factory.getStyle("Arial", 12, "Red");

        assertSame(first, second);
        assertEquals(1, factory.getStyleCount());
    }

    @Test
    public void testDifferentStyles() {
        CharacterStyleFactory factory =
                new CharacterStyleFactory();

        CharacterStyle first =
                factory.getStyle("Arial", 12, "Red");

        CharacterStyle second =
                factory.getStyle("Calibri", 14, "Blue");

        assertNotSame(first, second);
        assertEquals(2, factory.getStyleCount());
    }

    @Test
    public void testOnePropertyDifference() {
        CharacterStyleFactory factory =
                new CharacterStyleFactory();

        CharacterStyle original =
                factory.getStyle("Arial", 12, "Red");

        CharacterStyle changedSize =
                factory.getStyle("Arial", 14, "Red");

        CharacterStyle changedColor =
                factory.getStyle("Arial", 12, "Blue");

        CharacterStyle changedFont =
                factory.getStyle("Verdana", 12, "Red");

        assertAll(
                () -> assertNotSame(original, changedSize),
                () -> assertNotSame(original, changedColor),
                () -> assertNotSame(original, changedFont),
                () -> assertEquals(4, factory.getStyleCount())
        );
    }
}
