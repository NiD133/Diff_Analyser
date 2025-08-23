package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that reading lines from a BoundedReader stops at the boundary.
     * Specifically, it verifies that once the character limit is reached,
     * subsequent calls to BufferedReader.readLine() return null, indicating the
     * end of the stream.
     */
    @Test
    void testReadLinePastBoundaryReturnsNull() throws IOException {
        // Arrange: Create a BoundedReader with a 3-character limit on an input
        // string that is longer and contains no newlines.
        final String input = "01234567890";
        final StringReader sourceReader = new StringReader(input);
        final BoundedReader boundedReader = new BoundedReader(sourceReader, 3);

        // Act & Assert
        try (BufferedReader bufferedReader = new BufferedReader(boundedReader)) {
            // The first readLine() should consume the first 3 characters ("012"),
            // as the BoundedReader will report EOF after the 3rd character.
            assertEquals("012", bufferedReader.readLine());

            // The second readLine() should immediately return null because the
            // BoundedReader has already reached its limit.
            assertNull(bufferedReader.readLine(), "Should return null after boundary is reached");
        }
    }
}