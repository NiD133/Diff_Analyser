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

    /**
     * Verifies that calling skip() with a negative argument returns 0, as specified by the
     * {@link InputStream#skip(long)} contract, which states that no bytes should be skipped.
     */
    @Test
    public void skip_withNegativeArgument_shouldSkipZeroBytes() throws IOException {
        // Arrange: Create a MultiInputStream from an empty iterator. The stream's content
        // is not relevant for this test, as the check for a negative argument should
        // occur before any data is processed.
        Iterator<ByteSource> emptyIterator = Collections.emptyIterator();
        MultiInputStream stream = new MultiInputStream(emptyIterator);

        // Act: Attempt to skip a negative number of bytes.
        long bytesSkipped = stream.skip(-1L);

        // Assert: Verify that zero bytes were skipped.
        assertEquals(0L, bytesSkipped);
    }
}