package org.example;

import org.junit.jupiter.api.Test; // Using JUnit 5 for better readability
import static org.junit.jupiter.api.Assertions.assertEquals; // Using JUnit 5 assertions
import java.io.IOException;

/**
 * Test cases for the NullReader class.  Focuses on verifying the 'read' method.
 * This class aims to provide clearer and more maintainable tests.
 */
class NullReaderTest {  // Renamed class for clarity

    /**
     * Tests the behavior of the read(char[], int, int) method of NullReader
     * when attempting to read a large number of characters.
     *
     * This test creates a NullReader with a specific length (1592) and then attempts
     * to read a very large number of characters into a character array, starting at a
     * high index and attempting to read a large number of characters.  The expected behavior
     * is that it will read the number of characters defined by the reader's length.
     *
     * @throws IOException if an I/O error occurs (unlikely in this case, but the signature requires it)
     */
    @Test
    void testReadWithLargeParameters() throws IOException {
        // Arrange:  Create a NullReader with a specific length.  This acts as the setup.
        long readerLength = 1592L;
        NullReader nullReader = new NullReader(readerLength);
        char[] charArray = new char[5]; // A small char array

        // Act: Call the read method with a large offset and length. This is the action we are testing.
        int bytesRead = nullReader.read(charArray, 2146694131, 2146694131);

        // Assert: Verify the expected behavior.  This confirms our expectations.
        // 1. The number of bytes read should match the initial length of the reader.
        assertEquals(readerLength, bytesRead, "The number of bytes read should equal the reader's length.");

        // 2. Verify that the reader's position has advanced to the expected value.
        assertEquals(readerLength, nullReader.getPosition(), "The reader's position should be updated correctly.");
    }
}