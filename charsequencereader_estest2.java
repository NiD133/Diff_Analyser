package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void read_intoLargerBuffer_readsAllAvailableChars() throws IOException {
        // Arrange
        // Create a reader with a source sequence that is smaller than the destination buffer.
        String sourceSequence = "abc";
        CharSequenceReader reader = new CharSequenceReader(sourceSequence);
        char[] destinationBuffer = new char[10];
        
        // Act
        // Attempt to read from the source into the larger buffer.
        int charsRead = reader.read(destinationBuffer);

        // Assert
        // Verify that the number of characters read is equal to the length of the source sequence.
        assertEquals(sourceSequence.length(), charsRead);

        // Also, verify that the content was correctly copied into the buffer.
        char[] expectedBufferContent = new char[]{'a', 'b', 'c', '\0', '\0', '\0', '\0', '\0', '\0', '\0'};
        assertArrayEquals(expectedBufferContent, destinationBuffer);
    }
}