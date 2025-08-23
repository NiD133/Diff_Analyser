package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link BoundedReader} to verify its behavior with a closed underlying reader.
 */
public class BoundedReaderClosedTest {

    /**
     * Verifies that an attempt to read from a BoundedReader wrapping a closed
     * reader correctly propagates the IOException from the underlying reader.
     */
    @Test
    public void testReadOnClosedReaderThrowsIOException() throws IOException {
        // Arrange: Create an underlying reader and close it immediately.
        Reader closedUnderlyingReader = new StringReader("This content will not be read.");
        closedUnderlyingReader.close();

        // The BoundedReader wraps the already closed reader.
        final BoundedReader boundedReader = new BoundedReader(closedUnderlyingReader, 100);
        final char[] buffer = new char[10];

        // Act & Assert
        try {
            boundedReader.read(buffer, 0, buffer.length);
            fail("Expected an IOException because the underlying reader is closed.");
        } catch (final IOException e) {
            // The message "Stream closed" is thrown by the underlying StringReader
            // and should be propagated by the BoundedReader.
            assertEquals("Stream closed", e.getMessage());
        }
    }
}