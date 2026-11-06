
package flyweight;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextDocumentTest {

    @Test
    public void testAppendCharacters() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('H', "Arial", 12, "Red");
        doc.append('i', "Arial", 12, "Red");

        assertEquals("Hi", doc.getText());
        assertEquals(2, doc.length());
    }

    @Test
    public void testInsertCharacter() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('H', "Arial", 12, "Red");
        doc.append('i', "Arial", 12, "Red");
        doc.insert(1, '!', "Calibri", 14, "Blue");

        assertEquals("H!i", doc.getText());
    }

    @Test
    public void testReplaceCharacter() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('A', "Arial", 12, "Red");
        doc.replace(0, 'B');

        assertEquals("B", doc.getText());
    }

    @Test
    public void testDeleteCharacter() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('A', "Arial", 12, "Red");
        doc.append('B', "Arial", 12, "Red");
        doc.delete(0);

        assertEquals("B", doc.getText());
        assertEquals(1, doc.length());
    }

    @Test
    public void testFormattingChanges() {
        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument doc = new TextDocument(factory);

        doc.append('A', "Arial", 12, "Red");
        doc.append('B', "Arial", 12, "Red");

        assertSame(
                doc.getCharacter(0).getStyle(),
                doc.getCharacter(1).getStyle()
        );

        doc.format(0, "Verdana", 16, "Black");

        assertEquals("Verdana", doc.getCharacter(0).getStyle().getFont());
        assertEquals("Arial", doc.getCharacter(1).getStyle().getFont());
        assertEquals("AB", doc.getText());
        assertEquals(2, factory.getStyleCount());
    }

    @Test
    public void testSamePropertiesShareObject() {
        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument doc = new TextDocument(factory);

        doc.append('X', "Calibri", 14, "Blue");
        doc.append('Y', "Calibri", 14, "Blue");

        assertSame(
                doc.getCharacter(0).getStyle(),
                doc.getCharacter(1).getStyle()
        );

        assertEquals(1, factory.getStyleCount());
    }

    @Test
    public void testInvalidIndex() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        assertThrows(
                IndexOutOfBoundsException.class,
                () -> doc.replace(0, 'X')
        );
    }

    @Test
    public void testEmptyDocument() {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        assertEquals("", doc.getText());
        assertEquals(0, doc.length());
    }
}
