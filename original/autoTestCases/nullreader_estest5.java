package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals; // Simplified assertion import

public class NullReaderSkipTest { // Renamed class for clarity

    @Test
    public void testSkipAdvancesPosition() throws IOException { // More descriptive test name
        // Arrange: Create a NullReader that simulates reading from a null source.
        // Parameters: initialPosition = 1451, markSupported = true, throwEofException = true
        NullReader nullReader = new NullReader(1451L, true, true);

        // Act: Skip 1451 bytes using the skip() method.
        long bytesSkipped = nullReader.skip(1451L);

        // Assert: Verify that the number of bytes skipped matches the requested amount, and
        //         that the current position of the NullReader has been advanced accordingly.
        assertEquals(1451L, bytesSkipped, "The skip() method should return the number of bytes skipped.");
        assertEquals(2902L, nullReader.getPosition(), "The position should be advanced by the number of bytes skipped (1451 + 1451 = 2902).");
    }
}