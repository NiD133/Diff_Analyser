package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}, focusing on the interaction between the overall
 * character bound and the mark/reset functionality.
 */
public class BoundedReaderTest {

    /**
     * Tests that the BoundedReader respects the read-ahead limit set by mark()
     * by returning EOF, even if the reader's overall character bound has not been reached.
     * This is a key feature to prevent the underlying reader's mark from being invalidated.
     */
    @Test
    void readShouldStopAtMarkedReadAheadLimitAfterReset() throws IOException {
        // Arrange
        final String content = "01234567890";
        // Use a BufferedReader as it properly supports mark/reset.
        final StringReader stringReader = new StringReader(content);
        final BufferedReader bufferedReader = new BufferedReader(stringReader);

        // Create a BoundedReader with a maximum of 3 characters.
        try (BoundedReader boundedReader = new BoundedReader(bufferedReader, 3)) {

            // --- Part 1: Verify the primary bound works as expected ---

            // Act: Read all 3 characters, which is the reader's total limit.
            boundedReader.mark(3); // Mark at start, allowing 3 chars to be read before invalidation.
            assertEquals('0', boundedReader.read());
            assertEquals('1', boundedReader.read());
            assertEquals('2', boundedReader.read());

            // Assert: The next read should return EOF as the bound of 3 is reached.
            assertEquals(-1, boundedReader.read(), "Should return EOF after reaching the max character limit of 3.");

            // --- Part 2: Reset and verify the readAheadLimit is also enforced ---

            // Act: Reset to the beginning and set a new, smaller read-ahead limit.
            boundedReader.reset();
            boundedReader.mark(1); // New limit: only 1 character can be read before hitting the mark's limit.

            // Assert: We can read the first character successfully after the reset.
            assertEquals('0', boundedReader.read(), "Should read the first character after reset.");

            // Assert: The next read should return EOF because the read-ahead limit (1) is now reached.
            // This happens even though the total characters read since reset (1) is less than the overall bound (3).
            assertEquals(-1, boundedReader.read(), "Should return EOF after reaching the new readAheadLimit of 1.");
        }
    }
}