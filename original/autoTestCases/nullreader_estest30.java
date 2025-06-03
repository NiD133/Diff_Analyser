package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for clarity
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions

public class NullReaderTest {  // Renamed class for clarity

    @Test
    void testReadBeyondEOF() throws IOException {  // More descriptive test name
        // Arrange: Create a NullReader with a negative size.  This simulates an immediate EOF.
        NullReader nullReader = new NullReader(-970L);

        // Act: Attempt to read data.
        char[] buffer = new char[14]; // Create a buffer to read into
        nullReader.read(buffer); // Attempt to read into the buffer

        // Act:  Attempt to read a single character after the first read.
        int result = nullReader.read();

        // Assert:
        // 1. The NullReader's position should still be the initial negative value
        assertEquals(-970L, nullReader.getPosition(), "Position should remain unchanged after reading past EOF.");

        // 2. Reading beyond EOF should return -1.
        assertEquals(-1, result, "Reading beyond EOF should return -1.");
    }
}