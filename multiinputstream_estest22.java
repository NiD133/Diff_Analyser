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

    @Test
    public void available_withNoSources_returnsZero() throws IOException {
        // Arrange: Create an iterator that contains no ByteSource elements.
        Iterator<ByteSource> emptyIterator = Collections.emptyIterator();

        // Act: Create a MultiInputStream from the empty iterator.
        MultiInputStream stream = new MultiInputStream(emptyIterator);

        // Assert: The number of available bytes should be 0, as there are no underlying streams.
        assertEquals(0, stream.available());
    }
}