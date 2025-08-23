package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    private static final int EOF = -1;

    /**
     * Tests that BoundedReader consistently returns EOF when the underlying
     * reader is empty, even after a mark has been set.
     */
    @Test
    public void readFromEmptyReaderShouldConsistentlyReturnEof() throws IOException {
        // Arrange
        final int arbitraryLimit = 100;
        final Reader emptyReader = new StringReader("");
        final BoundedReader boundedReader = new BoundedReader(emptyReader, arbitraryLimit);

        // Set a mark and perform an initial read, which will return EOF.
        boundedReader.mark(10);
        boundedReader.read();

        // Act
        // A second read should also return EOF, confirming the stream remains at its end.
        final int result = boundedReader.read();

        // Assert
        assertEquals("Subsequent reads from an empty reader should return EOF", EOF, result);
    }
}