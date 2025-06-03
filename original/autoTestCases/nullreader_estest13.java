package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

/**
 * Test case for the NullReader class.  This tests basic functionality.
 */
class NullReaderTest {  // Renamed class for clarity

    /**
     * Tests the constructor and getSize() method of NullReader.
     * Verifies that the reader reports the correct size and supports marking.
     */
    @Test
    void testGetSizeAndMarkSupported() {
        // Arrange: Create a NullReader with a specified size.
        long expectedSize = 1279L;
        NullReader nullReader = new NullReader(expectedSize);

        // Act: Get the size from the reader and check if marking is supported.
        long actualSize = nullReader.getSize();
        boolean markSupported = nullReader.markSupported();

        // Assert: Verify that the size matches the expected size and marking is supported.
        assertEquals(expectedSize, actualSize, "The reported size should match the constructed size."); // Added message for clarity
        assertTrue(markSupported, "NullReader should support marking.");  // Added message for clarity
    }
}