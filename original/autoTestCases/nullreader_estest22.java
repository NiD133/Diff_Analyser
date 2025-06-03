package org.example;

import org.junit.jupiter.api.Test;  // Use JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

// This test case is designed to verify the behavior of the NullReader class
// specifically its 'skip' method.  The NullReader class simulates reading from
// a stream that always returns null.

class NullReaderTest { // Rename class for better clarity

    @Test
    void testSkipWithNegativeSizeDoesNotAdvancePosition() throws IOException { // Descriptive test name
        // Arrange: Create a NullReader with a negative size (-1324L)
        // A negative size typically indicates that the reader can conceptually read infinitely.
        NullReader nullReader = new NullReader(-1324L);

        // Act: Attempt to skip 8 bytes.  Note that skipping a negative size NullReader should have no effect on the position.
        long skippedBytes = nullReader.skip(8);

        // Assert: Verify that the position remains unchanged at its initial value (-1324L).
        // Also verify that the skip method returned the initial position value of the reader.
        assertEquals(-1324L, nullReader.getPosition(), "Reader position should remain unchanged after skip.");
        assertEquals(-1324L, skippedBytes, "Skip method should return initial position when size is negative.");
    }
}