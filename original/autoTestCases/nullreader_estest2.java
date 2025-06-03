package org.example;

import org.junit.jupiter.api.Test; // Updated import for JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Updated import for JUnit 5

public class NullReaderTest { // Renamed class for clarity and consistency

    @Test
    public void testReadWithNegativeArgumentsDoesNotAdvancePosition() throws IOException {
        // Arrange: Create a NullReader with a negative length (which is allowed but not very useful)
        NullReader nullReader = new NullReader(-1L);

        // Arrange: Create a character array to read into
        char[] charArray = new char[8];

        // Act: Attempt to read with negative offset and length.  This is expected to do nothing.
        nullReader.read(charArray, -1, -3484);

        // Act: Read a single character. Since the previous read didn't advance the position, this should return 0 (EOF).
        int result = nullReader.read();

        // Assert: Verify that the position is still unchanged after the first read attempt.
        //         The constructor takes a long, and since the read() operation does nothing (due to the negative length),
        //         the initial position is -1L, and reading does not change it.
        assertEquals(-1L, nullReader.getPosition());

        // Assert: Verify that reading a single character after the failed read returns 0 (indicating EOF because the length is -1).
        assertEquals(0, result);
    }
}