package org.example;

import org.junit.jupiter.api.Test;  // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5

// Removed EvoSuite specific imports as they aren't necessary for understanding the core test

public class NullReaderTest { // Renamed class for better readability

    @Test
    void testGetSizeReturnsCorrectValue() { // Changed method name for clarity
        // Arrange: Create a NullReader with a specific size.
        long expectedSize = -1324L;
        NullReader nullReader = new NullReader(expectedSize);

        // Act: Get the size of the NullReader.
        long actualSize = nullReader.getSize();

        // Assert: Verify that the returned size matches the expected size.
        assertEquals(expectedSize, actualSize, "The getSize() method should return the initially specified size.");

        // Assert: Verify that the NullReader supports marking.
        assertTrue(nullReader.markSupported(), "NullReader should support marking.");
    }
}