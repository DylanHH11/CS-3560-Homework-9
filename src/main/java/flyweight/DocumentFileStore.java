
package flyweight;

import java.io.*;

public class DocumentFileStore {

    public void save(TextDocument document, File file) throws IOException {

        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {

            out.writeUTF("CS3560-FLYWEIGHT-V1");
            out.writeInt(document.length());

            for (int i = 0; i < document.length(); i++) {
                TextCharacter character = document.getCharacter(i);
                CharacterStyle style = character.getStyle();

                out.writeChar(character.getSymbol());
                out.writeUTF(style.getFont());
                out.writeInt(style.getSize());
                out.writeUTF(style.getColor());
            }
        }
    }

    public TextDocument load(File file, CharacterStyleFactory factory)
            throws IOException {

        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {

            String header = in.readUTF();

            if (!header.equals("CS3560-FLYWEIGHT-V1")) {
                throw new IOException("Invalid document format");
            }

            int count = in.readInt();

            if (count < 0) {
                throw new IOException("Invalid character count");
            }

            TextDocument document = new TextDocument(factory);

            for (int i = 0; i < count; i++) {
                char symbol = in.readChar();
                String font = in.readUTF();
                int size = in.readInt();
                String color = in.readUTF();

                document.append(symbol, font, size, color);
            }

            return document;
        }
    }
}
