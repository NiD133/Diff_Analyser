package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void read_whenSequenceHasOneCharacter_returnsThatCharacter() throws IOException {
        // Arrange: Create a reader for a simple, single-character sequence.
        // Using a String is clear, as it's a common CharSequence implementation.
        CharSequence sequence = "a";
        CharSequenceReader reader = new CharSequenceReader(sequence);

        // Act: Read the first character from the reader.
        int characterRead = reader.read();

        // Assert: Verify that the character read is the one from the original sequence.
        assertEquals('a', characterRead);
    }
}