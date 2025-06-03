import org.junit.jupiter.api.Test;  // Changed from JUnit 4 to JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Changed for JUnit 5

import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderTest {

    @Test
    void testReadBeyondBounds() throws IOException {
        // Arrange: Create a StringReader with some text.
        String text = "CU6^Ejr;7S;Ndl FK8";
        StringReader stringReader = new StringReader(text);

        // Arrange: Create a BoundedReader that initially allows reading up to -1821 characters (effectively none).
        // Then, create another BoundedReader wrapping the first, allowing reading up to 1 character.
        BoundedReader boundedReaderWithNegativeLimit = new BoundedReader(stringReader, -1821);
        BoundedReader boundedReader = new BoundedReader(boundedReaderWithNegativeLimit, 1);

        // Act: Mark the current position. Although this has no effect due to the negative limit.
        boundedReader.mark(0); // mark is effectively a no-op here

        // Act: Attempt to read a character.  Since the initial BoundedReader effectively blocks all reads, the outer one does too.
        int result = boundedReader.read();

        // Assert: The read operation should return -1, indicating the end of the stream.  The initial bound of -1821 dominates.
        assertEquals(-1, result);
    }
}