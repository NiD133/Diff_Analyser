package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5

public class NullReaderTest { // Renamed class for clarity

    @Test
    void testReadFromNullReader() throws IOException { // Renamed and documented the test

        // Arrange: Obtain an instance of the NullReader.
        NullReader nullReader = NullReader.INSTANCE;

        // Act: Call the getPosition() and read() methods on the NullReader.
        long initialPosition = nullReader.getPosition();
        int readResult = nullReader.read();

        // Assert:  Because NullReader represents the end of a stream,
        // we expect read() to return -1 (end-of-stream indicator).
        // Also, verifying initial position after read to guarantee it doesn't change
        assertEquals(-1, readResult, "Reading from NullReader should return -1 (end of stream).");
        assertEquals(initialPosition, nullReader.getPosition(), "Reading shouldn't change the reader position.");

    }
}