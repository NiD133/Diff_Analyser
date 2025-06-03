package org.example;

import org.junit.jupiter.api.Test; // Changed to JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.*; // JUnit 5 assertions
import java.io.IOException;

/**
 * Test case for the NullReader class.
 * Demonstrates behavior when reading and skipping beyond the end of the stream.
 */
class NullReaderTest {

    @Test
    void testSkipAfterEOF() throws IOException {
        // Arrange: Create a NullReader instance.  This reader simulates an empty stream.
        NullReader reader = new NullReader();

        // Act: Attempt to read an array of characters. The read operation with length 0 should not throw an error, though it won't read anything.
        char[] buffer = new char[5];
        reader.read(buffer, 640, 0);

        // Assert: Verify that attempting to skip after reading (or rather not reading) throws an IOException.
        IOException exception = assertThrows(IOException.class, () -> {
            reader.skip(0L); // Attempt to skip. Since NullReader simulates EOF, this will fail.
        });

        // Verify that the exception message matches the expected message.
        assertEquals("Skip after end of file", exception.getMessage());

        // Note:  The original test used try-catch and fail() which is less readable than assertThrows.
        //       The specific behavior of skip(0L) after an initial read with length zero is perhaps an edge case,
        //       but the test illustrates EOF handling in NullReader.
    }
}