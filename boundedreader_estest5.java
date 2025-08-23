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

    /**
     * Tests that calling read() with a length of 0 returns 0, as specified by the
     * {@link Reader#read(char[], int, int)} contract. This should be true even
     * when provided with an otherwise invalid offset.
     */
    @Test
    public void readWithZeroLengthShouldReturnZero() throws IOException {
        // Arrange
        final Reader reader = new StringReader("test data");
        final BoundedReader boundedReader = new BoundedReader(reader, 10);
        final char[] buffer = new char[5];
        final int invalidOffset = -1;
        final int zeroLength = 0;

        // Act
        final int charsRead = boundedReader.read(buffer, invalidOffset, zeroLength);

        // Assert
        assertEquals("Reading zero characters should always return 0", 0, charsRead);
    }
}