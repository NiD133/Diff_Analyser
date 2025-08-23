package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that calling read() with a length of zero is a no-op and returns 0,
     * as specified by the {@link java.io.Reader#read(char[], int, int)} contract.
     */
    @Test
    public void read_whenLengthIsZero_shouldReturnZero() throws IOException {
        // Arrange
        // The content of the source sequence is irrelevant when reading zero characters.
        CharSequence sourceSequence = CharBuffer.wrap(new char[]{'a', 'b', 'c'});
        CharSequenceReader reader = new CharSequenceReader(sourceSequence);
        char[] destinationBuffer = new char[10];

        // Act
        int charsRead = reader.read(destinationBuffer, 0, 0);

        // Assert
        assertEquals("The number of characters read should be 0 when the requested length is 0.", 0, charsRead);
    }
}