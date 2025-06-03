package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test case for demonstrating the `mark()` and `reset()` functionality of the {@link TestNullReader}.
 * This test focuses on verifying that `mark()` and `reset()` work as expected, including handling
 * scenarios where the read limit is exceeded and when no mark has been set.
 */
public class GeneratedTestCase {

    @Test
    public void testMarkAndReset() throws Exception {
        // --- Setup ---
        // Define initial position, read limit, and create a TestNullReader instance.
        int initialPosition = 0;
        int readLimit = 10; // The number of characters that can be read after marking the stream, and still be able to reset.
        try (Reader reader = new TestNullReader(100, true, false)) { // Length = 100, Mark Supported = true, Throw Exception on EOF = false

            // --- Verification: Mark Support ---
            assertTrue(reader.markSupported(), "Reader should support mark operations.");

            // --- Test Case 1: Resetting without Marking ---
            // Verify that attempting to reset the reader before marking throws an IOException.
            IOException resetException = assertThrows(IOException.class, reader::reset,
                    "Resetting without a mark should throw an IOException.");
            assertEquals("No position has been marked", resetException.getMessage(),
                    "IOException message should indicate no position was marked.");

            // --- Test Case 2: Reading Before Marking ---
            // Read a few characters before marking the stream.
            for (; initialPosition < 3; initialPosition++) {
                assertEquals(initialPosition, reader.read(), "Read characters before marking.");
            }

            // --- Action: Mark the Stream ---
            // Mark the current position of the reader.
            reader.mark(readLimit);

            // --- Test Case 3: Reading After Marking and Before Read Limit ---
            // Read characters after marking the stream, ensuring that the read limit is not exceeded.
            for (int i = 0; i < 3; i++) {
                assertEquals(initialPosition + i, reader.read(), "Read characters after marking.");
            }

            // --- Action: Reset the Stream ---
            // Reset the stream to the marked position.
            reader.reset();

            // --- Test Case 4: Reading After Resetting ---
            // Read characters after resetting to the marked position, up to the read limit + 1.
            for (int i = 0; i < readLimit + 1; i++) {
                assertEquals(initialPosition + i, reader.read(), "Read characters after resetting.");
            }

            // --- Test Case 5: Resetting After Exceeding Read Limit ---
            // Verify that attempting to reset the reader after exceeding the read limit throws an IOException.
            IOException readLimitException = assertThrows(IOException.class, reader::reset,
                    "Resetting after exceeding read limit should throw an IOException.");
            assertEquals("Marked position [" + initialPosition + "] is no longer valid - passed the read limit [" + readLimit + "]",
                    readLimitException.getMessage(), "IOException message should indicate read limit exceeded.");

        } // Reader is automatically closed.
    }
}