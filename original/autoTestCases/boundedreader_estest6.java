import org.junit.jupiter.api.Test; // Modern JUnit import
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*; // Modern JUnit assertions

/**
 * Test case for the BoundedReader class in Apache Commons IO.
 * This test focuses on the behavior of the BoundedReader after it has been closed.
 */
public class BoundedReaderClosedStateTest {

    /**
     * Tests that attempting to reset a BoundedReader after it has been closed
     * throws an IOException.  This verifies that the reader correctly enforces
     * its closed state.
     *
     * @throws IOException If an I/O error occurs (expected in this test).
     */
    @Test
    public void testResetAfterCloseThrowsIOException() throws IOException {
        // 1. Arrange:  Create a StringReader and a BoundedReader with a limit of 0 characters.
        StringReader stringReader = new StringReader("Some text");
        BoundedReader boundedReader = new BoundedReader(stringReader, 0);

        // 2. Act: Close the BoundedReader.  This simulates the reader being finished with.
        boundedReader.close();

        // 3. Assert:  Verify that calling reset() after close() throws an IOException
        //    indicating that the stream is closed.  The expected exception is StringReader's
        //    IOException, which is wrapped and thrown by BoundedReader.
        IOException exception = assertThrows(IOException.class, () -> {
            boundedReader.reset();
        });

        // Optional: Add more specific assertion to check the message of the exception
        assertEquals("Stream closed", exception.getMessage());
    }
}