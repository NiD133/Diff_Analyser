package com.google.common.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link CharStreams#skipFully(Reader, long)}.
 */
@RunWith(JUnit4.class)
public class CharStreamsSkipFullyTest {

    @Test
    public void skipFully_whenCalledSequentially_advancesReaderCorrectly() throws IOException {
        // Arrange
        String input = "abcdef";
        Reader reader = new StringReader(input);

        // Act & Assert: Read the first character to ensure the stream is not at the beginning.
        // Stream state: "bcdef"
        assertEquals('a', reader.read());

        // Act & Assert: Skip a single character.
        CharStreams.skipFully(reader, 1); // Skips 'b'
        // Stream state: "cdef"
        assertEquals('c', reader.read());

        // Act & Assert: Skip multiple characters.
        CharStreams.skipFully(reader, 2); // Skips 'd' and 'e'
        // Stream state: "f"
        assertEquals('f', reader.read());

        // Act & Assert: Verify the end of the stream has been reached.
        assertEquals("Should be at the end of the stream", -1, reader.read());
    }

    @Test
    public void skipFully_whenSkippingPastEnd_throwsEofException() {
        // Arrange
        Reader reader = new StringReader("abc");

        // Act & Assert
        // Attempting to skip 5 characters from a 3-character stream should fail.
        assertThrows(EOFException.class, () -> CharStreams.skipFully(reader, 5));
    }

    @Test
    public void skipFully_withZeroCharacters_doesNothing() throws IOException {
        // Arrange
        String input = "abc";
        Reader reader = new StringReader(input);

        // Act
        CharStreams.skipFully(reader, 0);

        // Assert
        // The next character should still be the first one.
        assertEquals('a', reader.read());
    }
}