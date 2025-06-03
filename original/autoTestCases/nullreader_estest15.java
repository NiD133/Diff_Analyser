package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for cleaner imports
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions
import java.io.EOFException;

/**
 * Test case for the NullReader class. This test specifically focuses on
 * verifying the EOFException behavior when attempting to read from a
 * NullReader configured to immediately signal the end of the stream.
 */
class NullReaderTest {

    @Test
    void testReadEOFException() {
        // Arrange: Create a NullReader that immediately returns EOF (length 0, throwEOF true).
        NullReader nullReader = new NullReader(0L, true, true);

        // Arrange: Prepare a character array to read into (although it won't be populated).
        char[] charArray = new char[7];

        // Act & Assert: Attempt to read 0 characters from the NullReader.  This *should* throw an EOFException.
        assertThrows(EOFException.class, () -> {
            nullReader.read(charArray, 0, 0); // Try to read 0 characters starting at index 0.
        }, "Expected EOFException when reading from a NullReader configured to throw EOF immediately.");
    }
}