package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5
import java.io.IOException;

public class NullReaderTest { // Renamed class for clarity

    @Test
    void testProcessCharsDoesNotThrowException() throws IOException {
        // Arrange: Set up the test environment
        NullReader reader = new NullReader();
        char[] buffer = new char[2];
        int offset = 0; // Using a more reasonable offset for testing
        int length = 2; // Using a length that fits within the buffer size

        // Act: Execute the method under test
        reader.processChars(buffer, offset, length);

        // Assert: Verify the expected behavior
        // This test primarily verifies that the method completes without throwing an exception.
        // Since NullReader doesn't actually modify the buffer or throw exceptions in its implementation,
        // we can confirm that the operation succeeded by simply ensuring it doesn't crash.

        assertTrue(reader.markSupported(), "NullReader should always support marking."); // More descriptive message

        // Option 1: Check if the buffer is unchanged (if NullReader is supposed to do nothing)
        // char[] expectedBuffer = new char[2];  // Initialize with the expected default values of char array (null)
        // assertArrayEquals(expectedBuffer, buffer, "Buffer should remain unchanged after processChars.");

        // Option 2: If you expect the NullReader to fill the buffer with specific values:
        // char[] expectedBuffer = new char[]{'a', 'b'}; // Example: If processChars is intended to populate the array
        // assertArrayEquals(expectedBuffer, buffer, "Buffer should contain the expected values after processChars.");
    }
}