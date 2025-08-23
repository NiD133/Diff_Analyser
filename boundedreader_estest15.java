package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling mark() on a BoundedReader initialized with a null
     * underlying reader throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void markShouldThrowNullPointerExceptionWhenReaderIsNull() throws IOException {
        // Arrange: Create a BoundedReader with a null target reader.
        // The specific value for maxChars is irrelevant for this test.
        final int irrelevantMaxChars = 100;
        final BoundedReader boundedReader = new BoundedReader(null, irrelevantMaxChars);

        // Act & Assert: Calling mark() should throw a NullPointerException because it
        // attempts to delegate to the null target reader.
        final int irrelevantReadAheadLimit = 100;
        boundedReader.mark(irrelevantReadAheadLimit);
    }
}