package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that calling read() after the end of the file has been reached
     * throws an IOException.
     */
    @Test
    public void testReadAfterEndOfFileThrowsIOException() throws IOException {
        // Arrange: Use the singleton instance which has a size of 0.
        // This means it is already at the end of the file.
        final NullReader reader = NullReader.INSTANCE;

        // Act 1: The first read should return -1 to signal the end of the file.
        assertEquals("Expected end of file on first read", -1, reader.read());

        // Act 2 & Assert: A subsequent read should throw an IOException.
        try {
            reader.read();
            fail("Expected an IOException to be thrown when reading past the end of the file.");
        } catch (final IOException e) {
            // Verify the exception has the expected message.
            assertEquals("Read after end of file", e.getMessage());
        }
    }
}