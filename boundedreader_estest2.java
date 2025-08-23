package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that read() continues to return EOF on an exhausted stream,
     * even after calling mark(). This ensures that marking at the end of the
     * stream doesn't incorrectly change the reader's state.
     */
    @Test
    public void shouldReturnEofOnReadAfterMarkingAtEndOfStream() throws IOException {
        // Arrange: A BoundedReader wrapping an empty, and thus already exhausted, reader.
        // The bound is set to a positive value that won't be reached.
        final StringReader emptyReader = new StringReader("");
        final BoundedReader boundedReader = new BoundedReader(emptyReader, 100);

        // Act:
        // 1. Read once to ensure the reader is at the end of the stream.
        // 2. Mark the current position (which is the end).
        boundedReader.read(); // This first read returns EOF.
        boundedReader.mark(1);

        // Assert: A subsequent read should also return EOF.
        final int result = boundedReader.read();
        assertEquals("Reading after marking at the end of the stream should return EOF.", EOF, result);
    }
}