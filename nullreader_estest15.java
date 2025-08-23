package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that attempting to read from a NullReader after it has already
     * reached the end of the file (EOF) throws an IOException.
     */
    @Test
    public void testReadAfterEofThrowsIOException() throws IOException {
        // Arrange: Create a reader with size 0, which is immediately at EOF.
        // The first read will return -1 and set an internal 'eof' flag.
        final NullReader reader = new NullReader(0);
        reader.read(); // This call establishes the EOF condition.

        // Act & Assert: A subsequent read should throw an IOException.
        try {
            char[] buffer = new char[10];
            reader.read(buffer, 0, buffer.length);
            fail("Expected an IOException to be thrown when reading after EOF.");
        } catch (final IOException e) {
            // Assert that the correct exception was thrown.
            final String expectedMessage = "Read after end of file";
            assertEquals("The exception message was not as expected.", expectedMessage, e.getMessage());
        }
    }
}