package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    private static final int END_OF_STREAM = -1;

    /**
     * Verifies that reading from a MultiInputStream created with an empty
     * iterator immediately returns -1, indicating the end of the stream.
     */
    @Test
    public void read_withEmptyIterator_returnsEndOfStream() throws IOException {
        // Arrange: Create a MultiInputStream from an empty iterator of ByteSources.
        // This simulates a scenario with no input files or data sources.
        Iterator<ByteSource> emptySourceIterator = Collections.emptyIterator();
        InputStream multiInputStream = new MultiInputStream(emptySourceIterator);
        
        byte[] buffer = new byte[1024];

        // Act: Attempt to read from the empty stream.
        int bytesRead = multiInputStream.read(buffer, 0, buffer.length);

        // Assert: The read operation should return -1, as there is no data to read.
        assertEquals("Reading from an empty MultiInputStream should immediately signal end-of-stream.",
                END_OF_STREAM, bytesRead);
    }
}