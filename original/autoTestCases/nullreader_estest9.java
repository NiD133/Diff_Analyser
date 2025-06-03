package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

public class NullReaderTest { // Renamed class for better clarity

    @Test
    void testReadCharArray() throws IOException { // Renamed and clarified test method name
        // Arrange: Create a NullReader with a specific length and behavior
        long initialLength = 1480L;
        boolean markSupported = true; // Indicate if mark() and reset() are supported
        boolean throwIOException = true; //  Indicate whether to throw IOException when EOF is reached.
        NullReader reader = new NullReader(initialLength, markSupported, throwIOException);

        // Arrange: Create a character array to read into
        char[] buffer = new char[3];

        // Act: Read characters from the NullReader into the buffer
        int bytesRead = reader.read(buffer);

        // Assert: Verify that the expected number of characters were read
        assertEquals(3, bytesRead, "Expected 3 characters to be read.");

        // Assert: Verify that the reader's position has advanced correctly
        assertEquals(3L, reader.getPosition(), "Reader position should be 3 after reading 3 characters.");
    }
}