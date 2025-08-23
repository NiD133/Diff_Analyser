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

    @Test
    public void skip_onStreamWithSingleEmptySource_returnsZero() throws IOException {
        // Arrange: Create a MultiInputStream from an iterator over a single, empty ByteSource.
        ByteSource emptySource = ByteSource.empty();
        Iterator<ByteSource> sourceIterator = Collections.singletonList(emptySource).iterator();
        InputStream multiStream = new MultiInputStream(sourceIterator);

        long arbitraryBytesToSkip = 2173L;

        // Act: Attempt to skip bytes from the stream.
        long bytesSkipped = multiStream.skip(arbitraryBytesToSkip);

        // Assert: Verify that zero bytes were actually skipped because the underlying source is empty.
        assertEquals("Should not be able to skip any bytes from an empty source.", 0L, bytesSkipped);
    }
}