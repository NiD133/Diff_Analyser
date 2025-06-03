package org.example;

import org.junit.jupiter.api.Test; // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5

public class NullReaderTest { // Renamed class to be more descriptive

    @Test
    void testReadZeroCharsFromArrayWithOffset() throws IOException {
        // Arrange: Set up the test environment.
        long initialSize = 1279L;
        NullReader nullReader = new NullReader(initialSize); // Creates a NullReader with a defined size.
        char[] charArray = new char[4]; // Creates a character array to read into.
        int offset = 938; // Offset beyond the array's size.
        int length = 0; // Number of characters to read (zero in this case).

        // Act: Execute the method under test.
        int bytesRead = nullReader.read(charArray, offset, length); // Attempts to read zero characters.

        // Assert: Verify the expected outcome.
        assertTrue(nullReader.markSupported(), "NullReader should support mark/reset."); // Checks if mark/reset is supported.
        assertEquals(0, bytesRead, "Should return 0 when reading zero characters."); // Asserts that no characters are read (returns 0).
        assertEquals(0L, nullReader.getPosition(), "Position should not change when reading zero characters."); // Asserts that the reader's position hasn't changed.
    }
}