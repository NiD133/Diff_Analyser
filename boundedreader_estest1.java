package org.apache.commons.io.input;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that reading into a buffer larger than the stream's bound
     * will only read up to the specified bound.
     */
    @Test
    public void readIntoLargeBufferShouldStopAtBound() throws IOException {
        // Arrange
        final String content = "abcdefg";
        final int bound = 3;
        final char[] buffer = new char[content.length()]; // A buffer larger than the bound

        try (final StringReader stringReader = new StringReader(content);
             final BoundedReader boundedReader = new BoundedReader(stringReader, bound)) {

            // Act
            final int charsRead = boundedReader.read(buffer);

            // Assert
            // 1. Verify that the number of characters read is equal to the bound.
            assertEquals(bound, charsRead);

            // 2. Verify the buffer's content is correct.
            final char[] expectedBuffer = {'a', 'b', 'c', '\0', '\0', '\0', '\0'};
            assertArrayEquals(expectedBuffer, buffer);
        }
    }
}