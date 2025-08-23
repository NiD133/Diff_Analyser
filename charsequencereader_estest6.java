package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void read_intoCharArray_returnsCorrectCharacterCountWhenSourceHasEnoughChars() throws IOException {
        // Arrange
        String sourceText = "abcdef"; // Use a readable string for the source
        CharSequenceReader reader = new CharSequenceReader(sourceText);
        
        char[] destinationBuffer = new char[sourceText.length()];
        int offset = 0;
        int charsToRead = 4;

        // Act
        int charsRead = reader.read(destinationBuffer, offset, charsToRead);

        // Assert
        assertEquals("Should report reading the requested number of characters.", charsToRead, charsRead);
    }
}