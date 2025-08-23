package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that reading from a MultiInputStream created with an empty iterator
     * immediately returns -1, indicating the end of the stream.
     */
    @Test
    public void read_withEmptyIterator_returnsEndOfStream() throws IOException {
        // Arrange: Create an iterator that contains no ByteSource elements.
        Iterator<ByteSource> emptyIterator = Collections.emptyIterator();
        MultiInputStream stream = new MultiInputStream(emptyIterator);

        // Act: Attempt to read a byte from the stream.
        int result = stream.read();

        // Assert: The read operation should return -1, the standard indicator for end-of-stream.
        assertEquals(-1, result);
    }
}