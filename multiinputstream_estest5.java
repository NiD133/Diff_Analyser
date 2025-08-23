package com.google.common.io;

import static java.util.Collections.emptyIterator;

import java.io.IOException;
import java.util.Iterator;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    /**
     * Verifies that calling read() with a null buffer throws a NullPointerException,
     * as required by the InputStream contract.
     */
    @Test(expected = NullPointerException.class)
    public void read_withNullBuffer_throwsNullPointerException() throws IOException {
        // Arrange: Create a MultiInputStream. The source of the stream is irrelevant
        // for this precondition check, so an empty one is used for simplicity.
        Iterator<ByteSource> emptySourceIterator = emptyIterator();
        MultiInputStream stream = new MultiInputStream(emptySourceIterator);

        // Act: Attempt to read into a null buffer. The invalid offset and length values
        // are secondary, as the null buffer check should have precedence.
        stream.read(null, -1, -1);

        // Assert: The test passes if a NullPointerException is thrown, which is
        // handled by the 'expected' attribute on the @Test annotation.
    }
}