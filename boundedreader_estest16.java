package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling mark() with a negative read-ahead limit propagates
     * the IllegalArgumentException from the underlying reader.
     * <p>
     * The BoundedReader should delegate the mark() call, and any resulting
     * exception, to the wrapped reader.
     */
    @Test
    public void markWithNegativeReadAheadLimitShouldPropagateExceptionFromUnderlyingReader() throws IOException {
        // Arrange: Create a BoundedReader with a standard underlying reader.
        // The content of the reader and the max characters to read are not relevant for this test.
        final Reader underlyingReader = new StringReader("test data");
        final BoundedReader boundedReader = new BoundedReader(underlyingReader, 10);
        final int invalidReadAheadLimit = -1;

        // Act & Assert
        try {
            boundedReader.mark(invalidReadAheadLimit);
            fail("Expected an IllegalArgumentException to be thrown for a negative read-ahead limit.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception from the underlying reader was propagated correctly.
            // The StringReader.mark() implementation throws an exception with this specific message.
            assertEquals("Read-ahead limit < 0", e.getMessage());
        }
    }
}