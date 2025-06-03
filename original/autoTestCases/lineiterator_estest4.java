import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * Test case to verify the behavior of LineIterator when used with a closed BufferedReader.
 */
public class LineIteratorClosedReaderTest {

    @Test
    public void testNextLine_AfterBufferedReaderIsClosed_ThrowsIllegalStateException() throws IOException {
        // Arrange: Create a StringReader and wrap it in a BufferedReader.
        StringReader stringReader = new StringReader("Some text");
        BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Act: Close the BufferedReader.  This makes any further reads invalid.
        bufferedReader.close();

        // Create a LineIterator that uses the closed BufferedReader.
        LineIterator lineIterator = new LineIterator(bufferedReader);

        // Assert: Calling nextLine() on the LineIterator should throw an IllegalStateException
        // because the underlying BufferedReader is closed.
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            lineIterator.nextLine();
        });

        // Optionally, assert that the exception message indicates the reason for the failure.
        assertEquals("java.io.IOException: Stream closed", exception.getMessage()); // Or similar message
    }
}