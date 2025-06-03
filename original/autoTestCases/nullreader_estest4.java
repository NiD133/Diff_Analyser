package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 annotations for better readability
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

/**
 * Test case for the NullReader class.  This test focuses on the skip() method.
 */
public class NullReaderTest { // Renamed class for clarity and convention

    @Test
    void testSkipZeroBytes() throws IOException { // More descriptive test method name
        // Arrange: Create a NullReader that simulates reading from a null stream.
        // The constructor argument (597L) defines the initial size of the simulated stream.
        NullReader nullReader = new NullReader(597L);

        // Act: Attempt to skip 0 bytes in the stream.
        long skipped = nullReader.skip(0);

        // Assert:  Verify that the skip() method returned 0, indicating that no bytes were skipped (as expected).
        assertEquals(0L, skipped, "Skipping 0 bytes should return 0."); // Added message for clearer assertion failure

        // Assert: Verify that the NullReader still supports mark/reset operations. This is a default behavior and important to ensure it's not broken.
        assertTrue(nullReader.markSupported(), "NullReader should support mark/reset operations.");
    }
}