package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#toString(Readable)}.
 */
// The original test class name "CharStreams_ESTestTest38" was likely auto-generated.
// A more descriptive name like "CharStreamsTest" is used here for clarity.
public class CharStreamsTest {

    @Test
    public void toString_readsAllCharactersFromReadable() throws IOException {
        // Arrange
        // A char array is initialized with null characters ('\u0000') by default.
        // This test verifies that even non-printable characters are handled correctly.
        char[] inputCharacters = new char[3];
        Reader reader = new CharArrayReader(inputCharacters);
        String expectedString = new String(inputCharacters); // The expected string is "\u0000\u0000\u0000"

        // Act
        String actualString = CharStreams.toString(reader);

        // Assert
        assertEquals(expectedString, actualString);
    }
}