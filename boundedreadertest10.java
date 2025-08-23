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
public class BoundedReaderTest {

    /**
     * Tests that reading stops at the specified boundary, even if a mark has been set.
     * The BoundedReader's character limit should always have precedence over the underlying
     * reader's state or the mark's read-ahead limit.
     */
    @Test
    void readShouldReturnEofWhenBoundIsReachedEvenAfterMarking() throws IOException {
        // Arrange
        final String content = "0123456789";
        final int maxCharsToRead = 3;
        final Reader sourceReader = new BufferedReader(new StringReader(content));

        try (final BoundedReader boundedReader = new BoundedReader(sourceReader, maxCharsToRead)) {
            // Read one character to advance the position before marking.
            boundedReader.read(); // Reads '0'

            // Set a mark. The BoundedReader should still respect its own boundary.
            boundedReader.mark(maxCharsToRead);

            // Read the next two characters, which will bring the total read count to the max.
            boundedReader.read(); // Reads '1'
            boundedReader.read(); // Reads '2'

            // Act: Attempt to read one more character, which is past the boundary.
            final int charReadPastBoundary = boundedReader.read();

            // Assert: Verify that the read operation returned EOF (-1).
            assertEquals(-1, charReadPastBoundary, "BoundedReader should return EOF when the max character limit is reached.");
        }
    }
}