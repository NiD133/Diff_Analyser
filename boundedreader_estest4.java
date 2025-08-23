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
     * Tests that reading from an empty underlying reader consistently returns EOF,
     * even after a mark has been set. The BoundedReader should correctly
     * propagate the EOF signal from the exhausted source.
     */
    @Test
    public void testReadFromEmptyReaderConsistentlyReturnsEof() throws IOException {
        // Arrange: Create a BoundedReader with an empty underlying source.
        // The max character limit is irrelevant here as the source is already empty.
        final int anyMaxCharLimit = 100;
        final StringReader emptySourceReader = new StringReader("");
        final BoundedReader boundedReader = new BoundedReader(emptySourceReader, anyMaxCharLimit);

        // Act & Assert: The first read should immediately return EOF.
        assertEquals("First read on an empty reader should return EOF.", EOF, boundedReader.read());

        // Arrange a subsequent action: mark the current position (which is the end of the stream).
        final int anyReadAheadLimit = 10;
        boundedReader.mark(anyReadAheadLimit);

        // Act & Assert: Reading again after the mark should still return EOF.
        assertEquals("Read after marking at EOF should also return EOF.", EOF, boundedReader.read());
    }
}