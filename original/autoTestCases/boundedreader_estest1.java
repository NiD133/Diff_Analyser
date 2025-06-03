package org.apache.commons.io.input;

import org.junit.jupiter.api.Test; // Using JUnit 5 for clarity and modern features
import static org.junit.jupiter.api.Assertions.*; // Using JUnit 5 assertions
import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;

/**
 * Test cases for the BoundedReader class in Apache Commons IO.  This focuses on
 * verifying that the BoundedReader correctly limits the number of characters read.
 */
public class BoundedReaderTest {  // Renamed class for better readability

    /**
     * Test case to verify that a BoundedReader with a limit of 0 returns -1 (end of stream)
     * immediately when reading into a CharBuffer.  This simulates reading from a reader
     * that has been artificially limited to no characters.
     */
    @Test
    public void testReadIntoCharBufferWithZeroLimit() throws IOException {
        // Arrange: Create a StringReader and a BoundedReader with a limit of 0.
        StringReader stringReader = new StringReader("TO[Gj");  // The content to be read (initially).
        BoundedReader boundedReader = new BoundedReader(stringReader, 0); // Limit the reader to 0 characters.

        // Arrange: Create a CharBuffer to read into. The content is not relevant for this test case.
        CharBuffer charBuffer = CharBuffer.wrap("TO[Gj"); // A buffer to read the characters into.

        // Act: Attempt to read from the BoundedReader into the CharBuffer.
        int bytesRead = boundedReader.read(charBuffer);

        // Assert: Verify that the read method returns -1, indicating end of stream
        // because the BoundedReader was initialized with a limit of 0.
        assertEquals(-1, bytesRead, "Expected read() to return -1 because the limit is 0.");
    }
}