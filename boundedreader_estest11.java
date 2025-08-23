package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Unit tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that the read(char[], int, int) method propagates an IndexOutOfBoundsException
     * from the underlying reader when the offset is invalid for the given buffer.
     */
    @Test
    public void readWithOffsetOutOfBoundsShouldPropagateException() throws IOException {
        // Arrange
        final Reader underlyingReader = new StringReader("This is the test data.");
        final BoundedReader boundedReader = new BoundedReader(underlyingReader, 100);
        
        final char[] buffer = new char[10];
        final int invalidOffset = 20; // An offset clearly outside the buffer's bounds
        final int length = 1;

        // Act & Assert
        // The BoundedReader should delegate the read call, and the underlying reader
        // will throw an exception because the offset is out of bounds.
        assertThrows(
            IndexOutOfBoundsException.class,
            () -> boundedReader.read(buffer, invalidOffset, length)
        );
    }
}