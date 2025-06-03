package org.example;

import org.junit.jupiter.api.Test;  // Changed to JUnit 5 for clarity and modern usage
import static org.junit.jupiter.api.Assertions.*; // More concise assertions

// Removed unused imports: EOFException, IOException, EvoRunner, EvoRunnerParameters, RunWith

/**
 * Test class for NullReader, focusing on the markSupported() method.
 * This test verifies that the markSupported() method of NullReader returns false when the reader does not support marking.
 */
class NullReaderTest { // Renamed for better readability and convention

    @Test
    void testMarkNotSupported() { // More descriptive name
        // Arrange: Create a NullReader instance that does not support marking.
        // Parameters: length=0, markSupported=false, readThrowsException=false
        NullReader nullReader = new NullReader(0L, false, false);

        // Act: Call the markSupported() method.
        boolean supportsMarking = nullReader.markSupported();

        // Assert: Verify that the method returns false.
        assertFalse(supportsMarking, "NullReader should not support marking when initialized with markSupported=false.");
    }
}