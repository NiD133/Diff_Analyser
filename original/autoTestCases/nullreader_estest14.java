package org.example;

import org.junit.jupiter.api.Test;  // Use JUnit 5 for clarity and modern features
import static org.junit.jupiter.api.Assertions.*; // Modern assertion style

class NullReaderTest {  // More descriptive class name

    @Test
    void testGetPositionAfterRead() throws IOException {  // More descriptive test name
        // Arrange: Create a NullReader that simulates reading from a stream with a specified size.
        NullReader nullReader = new NullReader(-961L); // The size doesn't really matter for this test.

        // Act: Read a single character from the NullReader. This advances the position.
        nullReader.read();

        // Assert: Verify that the position of the reader has been incremented to 1 after the read operation.
        assertEquals(1L, nullReader.getPosition(), "The reader's position should be 1 after reading one character.");
    }
}