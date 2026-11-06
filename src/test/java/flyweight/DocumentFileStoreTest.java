
package flyweight;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentFileStoreTest {

    @TempDir
    Path tempDir;

    @Test
    public void testSaveCreatesFile() throws IOException {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());
        doc.append('A', "Arial", 12, "Red");

        File file = tempDir.resolve("document.bin").toFile();

        new DocumentFileStore().save(doc, file);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testSaveAndLoadText() throws IOException {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('H', "Arial", 12, "Red");
        doc.append('i', "Calibri", 14, "Blue");

        File file = tempDir.resolve("document.bin").toFile();
        DocumentFileStore store = new DocumentFileStore();

        store.save(doc, file);

        TextDocument loaded =
                store.load(file, new CharacterStyleFactory());

        assertEquals("Hi", loaded.getText());
    }

    @Test
    public void testSaveAndLoadFormatting() throws IOException {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('A', "Arial", 12, "Red");
        doc.append('B', "Verdana", 16, "Black");

        File file = tempDir.resolve("document.bin").toFile();
        DocumentFileStore store = new DocumentFileStore();

        store.save(doc, file);

        TextDocument loaded =
                store.load(file, new CharacterStyleFactory());

        CharacterStyle style = loaded.getCharacter(1).getStyle();

        assertAll(
                () -> assertEquals("Verdana", style.getFont()),
                () -> assertEquals(16, style.getSize()),
                () -> assertEquals("Black", style.getColor())
        );
    }

    @Test
    public void testLoadedStylesAreShared() throws IOException {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        doc.append('A', "Arial", 12, "Red");
        doc.append('B', "Arial", 12, "Red");

        File file = tempDir.resolve("document.bin").toFile();
        DocumentFileStore store = new DocumentFileStore();

        store.save(doc, file);

        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument loaded = store.load(file, factory);

        assertSame(
                loaded.getCharacter(0).getStyle(),
                loaded.getCharacter(1).getStyle()
        );

        assertEquals(1, factory.getStyleCount());
    }

    @Test
    public void testEmptyDocument() throws IOException {
        TextDocument doc = new TextDocument(new CharacterStyleFactory());

        File file = tempDir.resolve("empty.bin").toFile();
        DocumentFileStore store = new DocumentFileStore();

        store.save(doc, file);

        TextDocument loaded =
                store.load(file, new CharacterStyleFactory());

        assertEquals("", loaded.getText());
        assertEquals(0, loaded.length());
    }

    @Test
    public void testMissingFile() {
        File missing = tempDir.resolve("missing.bin").toFile();

        DocumentFileStore store = new DocumentFileStore();

        assertThrows(
                IOException.class,
                () -> store.load(missing, new CharacterStyleFactory())
        );
    }

    @Test
    public void testInvalidFile() throws IOException {
        Path invalid = tempDir.resolve("invalid.bin");
        Files.writeString(invalid, "not a valid document");

        DocumentFileStore store = new DocumentFileStore();

        assertThrows(
                IOException.class,
                () -> store.load(invalid.toFile(), new CharacterStyleFactory())
        );
    }
}
