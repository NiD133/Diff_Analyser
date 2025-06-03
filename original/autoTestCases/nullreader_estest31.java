package org.example;

import org.junit.jupiter.api.Test; // Updated import for clarity and modern JUnit
import static org.junit.jupiter.api.Assertions.*; // Updated import for clarity

public class NullReaderTest { // Renamed class for better readability

    @Test
    void testMarkSupported_ReturnsTrue() { // More descriptive test name
        // Arrange: Create a NullReader instance with a negative size (valid for NullReader).
        long negativeSize = -1324L;
        NullReader nullReader = new NullReader(negativeSize);

        // Act: Call the markSupported() method.
        boolean supportsMark = nullReader.markSupported();

        // Assert: Verify that markSupported() returns true.
        assertTrue(supportsMark, "NullReader should support mark() operation.");

        // Assert: Verify the size is still as expected.  (Adding a check for state preservation)
        assertEquals(negativeSize, nullReader.getSize(), "Size should remain unchanged.");
    }
}