import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderExampleTest {

    @Test
    public void testReadCharArrayAndEOF() throws IOException {
        // Arrange: Create a StringReader with the input "QN?" and a BoundedReader limited to a large number of characters (2680).
        String inputString = "QN?";
        StringReader stringReader = new StringReader(inputString);
        BoundedReader boundedReader = new BoundedReader(stringReader, 2680);

        // Arrange: Create a char array to read into.
        char[] charArray = new char[9];

        // Act: Read from the BoundedReader into the char array.
        int charactersRead = boundedReader.read(charArray);

        // Assert: Verify that the correct number of characters was read.  The entire input "QN?" should be read.
        assertEquals(3, charactersRead);

        // Assert: Verify that the char array contains the expected characters.
        char[] expectedCharArray = {'Q', 'N', '?', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'};
        assertArrayEquals(expectedCharArray, charArray);

        // Act: Mark the current position in the reader.  This isn't really used in this specific example.
        boundedReader.mark(1);

        // Act: Attempt to read another character.  Since we've read all available characters, we expect an end-of-file (EOF).
        int nextCharacter = boundedReader.read();

        // Assert: Verify that the next character read is -1, indicating EOF.
        assertEquals(-1, nextCharacter);
    }
}