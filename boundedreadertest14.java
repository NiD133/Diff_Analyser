package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}.
 */
class BoundedReaderTest {

    /**
     * Tests that reading from a BoundedReader stops at the specified limit,
     * returning -1 (EOF) for subsequent reads, even if the underlying
     * reader has more data.
     */
    @Test
    void readShouldReturnEofWhenLimitIsReached() throws IOException {
        // Arrange
        final String content = "0123456789";
        final int limit = 3;
        final Reader underlyingReader = new StringReader(content);

        // The BoundedReader should only allow reading the first 3 characters: "012".
        try (final BoundedReader boundedReader = new BoundedReader(underlyingReader, limit)) {

            // Act & Assert
            // Read the first 3 characters, which are within the bound.
            assertEquals('0', boundedReader.read());
            assertEquals('1', boundedReader.read());
            assertEquals('2', boundedReader.read());

            // The next read should hit the bound and return EOF (-1).
            assertEquals(-1, boundedReader.read(), "Should return EOF after reading the specified number of characters.");
        }
    }
}