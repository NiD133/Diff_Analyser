package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader} focusing on mark and reset functionality.
 */
public class BoundedReaderTest {

    @Test
    void markAndResetShouldAllowReReadingWithinBounds() throws IOException {
        // Arrange
        final String content = "0123456789";
        final int bound = 3;
        final Reader sourceReader = new BufferedReader(new StringReader(content));
        try (final BoundedReader boundedReader = new BoundedReader(sourceReader, bound)) {
            // Set a mark at the beginning of the stream.
            boundedReader.mark(bound);

            // Act & Assert: First read sequence
            // Read up to the defined bound.
            assertEquals('0', boundedReader.read(), "First character should be '0'");
            assertEquals('1', boundedReader.read(), "Second character should be '1'");
            assertEquals('2', boundedReader.read(), "Third character should be '2'");
            assertEquals(-1, boundedReader.read(), "Should reach end of bound after 3 characters");

            // Act: Reset the stream to the last mark
            boundedReader.reset();

            // Assert: Second read sequence after reset
            // The reader should behave as if it was just created, allowing re-reading.
            assertEquals('0', boundedReader.read(), "First character after reset should be '0'");
            assertEquals('1', boundedReader.read(), "Second character after reset should be '1'");
            assertEquals('2', boundedReader.read(), "Third character after reset should be '2'");
            assertEquals(-1, boundedReader.read(), "Should reach end of bound again after reset and re-read");
        }
    }
}