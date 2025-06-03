package org.example;

import org.junit.jupiter.api.Test; // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5

/**
 * Test case for the NullReader class.  Specifically tests closing the reader
 * and verifying the position is reset to zero.
 */
public class NullReaderTest { // Renamed class to follow naming conventions

    @Test
    void testCloseResetsPositionToZero() throws IOException { // Renamed method for clarity
        // Arrange: Create an instance of NullReader
        NullReader nullReader = NullReader.INSTANCE;

        // Act: Close the NullReader
        nullReader.close();

        // Assert: Verify that the reader's position is now zero after closing.
        assertEquals(0L, nullReader.getPosition(), "After closing, the position should be 0.");
    }
}