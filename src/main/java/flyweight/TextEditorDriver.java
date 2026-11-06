
package flyweight;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TextEditorDriver {

    public static void main(String[] args) throws IOException {

        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument document = new TextDocument(factory);

        appendText(document, "Hello", "Arial", 12, "Red");
        appendText(document, "World", "Calibri", 14, "Blue");
        appendText(document, "CS", "Verdana", 16, "Black");
        appendText(document, "356X", "Arial", 16, "Blue");

        // Demonstrate editing an existing character.
        document.replace(document.length() - 1, '0');

        DocumentFileStore store = new DocumentFileStore();

        File file = new File("target", "HelloWorldCS3560.bin");
        Files.createDirectories(file.toPath().getParent());

        store.save(document, file);

        CharacterStyleFactory loadedFactory = new CharacterStyleFactory();
        TextDocument loaded = store.load(file, loadedFactory);

        if (!loaded.getText().equals("HelloWorldCS3560")) {
            throw new IllegalStateException("Document did not load correctly");
        }

        System.out.println("Saved text: " + document.getText());
        System.out.println("Loaded text: " + loaded.getText());
        System.out.println("Character count: " + loaded.length());
        System.out.println("Shared style count: " + loadedFactory.getStyleCount());

        int[] positions = {0, 5, 10, 12};

        for (int position : positions) {
            CharacterStyle style = loaded.getCharacter(position).getStyle();

            System.out.println(
                    loaded.getCharacter(position).getSymbol()
                            + ": " + style.getFont()
                            + ", " + style.getSize()
                            + ", " + style.getColor()
            );
        }

        System.out.println("Saved file: " + file.getPath());
    }

    private static void appendText(TextDocument document, String text,
                                   String font, int size, String color) {

        for (char character : text.toCharArray()) {
            document.append(character, font, size, color);
        }
    }
}
