package org.apache.commons.io.input;

import org.junit.Test;

import java.io.IOException;
import java.io.Reader;

/**
 * Tests for {@link BoundedReader} focusing on edge cases and invalid arguments.
 */
public class BoundedReaderTest {

    /**
     * Verifies that the read(char[], int, int) method throws a NullPointerException
     * when the BoundedReader is constructed with a null underlying reader.
     */
    @Test(expected = NullPointerException.class)
    public void testReadWithNullReaderThrowsNullPointerException() throws IOException {
        // Arrange: Create a BoundedReader with a null target reader.
        // The max size is arbitrary but required by the constructor.
        final int maxSize = 10;
        final BoundedReader boundedReader = new BoundedReader(null, maxSize);
        final char[] buffer = new char[5];

        // Act & Assert: Calling read should throw a NullPointerException
        // because it will attempt to delegate to the null reader.
        boundedReader.read(buffer, 0, buffer.length);
    }
}