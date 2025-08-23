package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}.
 */
class BoundedReaderTest {

    /**
     * Tests that BoundedReader returns EOF (-1) when the underlying reader is exhausted,
     * even if the character bound has not yet been reached.
     */
    @Test
    void whenUnderlyingReaderIsExhausted_readShouldReturnEof() throws IOException {
        // Arrange: An underlying reader with 2 characters and a BoundedReader with a larger limit of 3.
        final Reader underlyingReader = new BufferedReader(new StringReader("01"));
        try (final BoundedReader boundedReader = new BoundedReader(underlyingReader, 3)) {

            // Act & Assert: Read all available characters from the underlying reader.
            assertEquals('0', boundedReader.read(), "First character should be '0'");
            assertEquals('1', boundedReader.read(), "Second character should be '1'");

            // Assert: The next read should return EOF, as the underlying reader is now empty.
            assertEquals(-1, boundedReader.read(), "Should return EOF as underlying reader is exhausted, despite the bound not being met.");
        }
    }
}