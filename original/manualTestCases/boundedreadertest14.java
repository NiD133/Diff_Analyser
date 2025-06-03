package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoundedReaderTest {

    @Test
    public void testReadTillEnd() throws IOException {
        // Arrange:
        // Create a StringReader with a short string.
        StringReader stringReader = new StringReader("abc");

        // Create a BoundedReader that limits reading to 3 characters.
        BoundedReader boundedReader = new BoundedReader(stringReader, 3);

        // Act:
        // Read characters from the BoundedReader until it reaches the end.
        boundedReader.read(); // Read 'a'
        boundedReader.read(); // Read 'b'
        boundedReader.read(); // Read 'c'

        // Assert:
        // After reading 3 characters, the next read() should return -1, indicating the end of the stream.
        assertEquals(-1, boundedReader.read(), "The reader should return -1 after reading 3 characters.");

        // Clean up:  (Although not strictly necessary for StringReader, closing resources is good practice)
        boundedReader.close();
    }

    @Test
    public void testReadWithLengthLimit() throws IOException {
        // Arrange:
        StringReader stringReader = new StringReader("abcdefg");
        BoundedReader boundedReader = new BoundedReader(stringReader, 3);

        // Act:
        char[] buffer = new char[5];
        int bytesRead = boundedReader.read(buffer, 0, 5);

        // Assert:
        assertEquals(3, bytesRead, "Should read only 3 characters due to the bound.");
        assertEquals("abc\0\0", new String(buffer), "Buffer should contain 'abc' followed by padding.");

        boundedReader.close();
    }

    @Test
    public void testSkip() throws IOException {
        // Arrange:
        StringReader stringReader = new StringReader("abcdefg");
        BoundedReader boundedReader = new BoundedReader(stringReader, 5);

        // Act:
        long skipped = boundedReader.skip(2);

        // Assert:
        assertEquals(2, skipped, "Should skip 2 characters.");
        assertEquals('c', (char)boundedReader.read(), "Next read should return 'c'.");

        boundedReader.close();
    }

    @Test
    public void testMarkAndReset() throws IOException {
        // Arrange:
        StringReader stringReader = new StringReader("abcdefg");
        BoundedReader boundedReader = new BoundedReader(stringReader, 5);

        // Act:
        boundedReader.mark(10); // Mark at the beginning
        boundedReader.read(); // Read 'a'
        boundedReader.read(); // Read 'b'
        boundedReader.reset(); // Reset to the marked position

        // Assert:
        assertEquals('a', (char)boundedReader.read(), "Should return 'a' after reset.");

        boundedReader.close();
    }
}