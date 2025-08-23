package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link CharSequenceReader}.
 */
@RunWith(JUnit4.class)
public class CharSequenceReaderTest {

    @Test
    public void markSupported_shouldAlwaysReturnTrue() {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test data");

        // Act & Assert
        assertTrue("CharSequenceReader is expected to support marking.", reader.markSupported());
    }

    @Test
    public void reset_shouldReturnReaderToMarkedPosition() throws IOException {
        // Arrange
        String input = "abcdef";
        CharSequenceReader reader = new CharSequenceReader(input);

        // Act
        // 1. Read two characters to advance the reader's position.
        reader.read(); // reads 'a'
        reader.read(); // reads 'b', position is now at index 2

        // 2. Mark the current position (at 'c').
        // The readAheadLimit parameter is required by the Reader API but is not
        // used by this implementation.
        reader.mark(100);

        // 3. Read two more characters to move past the mark.
        reader.read(); // reads 'c'
        reader.read(); // reads 'd', position is now at index 4

        // 4. Reset the reader to the previously marked position.
        reader.reset();

        // Assert
        // The next character read should be 'c' (at index 2), not 'e'.
        int nextCharAfterReset = reader.read();
        assertEquals('c', (char) nextCharAfterReset);
    }
}