package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}, focusing on the interaction between mark/reset
 * and the reader's character bound.
 */
public class BoundedReaderTest {

    /**
     * Tests that calling mark() with a readAheadLimit greater than the BoundedReader's
     * maximum character limit still works correctly.
     * <p>
     * The reader should be able to reset and re-read, but it must still enforce its
     * original bound and return EOF once the total number of characters read reaches that bound.
     * </p>
     */
    @Test
    void testMarkWithReadAheadLimitGreaterThanBound() throws IOException {
        // Arrange
        final String inputData = "0123456789";
        final int bound = 3;
        // Use a readAheadLimit that is intentionally larger than the reader's bound
        // to test that the bound is still respected.
        final int readAheadLimit = bound + 1;
        final Reader underlyingReader = new StringReader(inputData);

        try (final BoundedReader boundedReader = new BoundedReader(underlyingReader, bound)) {
            // Act: Mark the initial position.
            boundedReader.mark(readAheadLimit);

            // Assert: Read up to the bound, verifying the content.
            assertEquals('0', boundedReader.read());
            assertEquals('1', boundedReader.read());
            assertEquals('2', boundedReader.read());

            // Act: Reset the reader to the marked position.
            boundedReader.reset();

            // Assert: We can read the same characters again after the reset.
            assertEquals('0', boundedReader.read());
            assertEquals('1', boundedReader.read());
            assertEquals('2', boundedReader.read());

            // Assert: The next read should return EOF, as we have hit the bound (3 chars).
            // This confirms the BoundedReader's limit is enforced even after a reset.
            assertEquals(-1, boundedReader.read(), "Reader should return EOF after reading the maximum number of characters.");
        }
    }
}