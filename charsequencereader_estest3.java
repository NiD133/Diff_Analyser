package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void skip_whenSkippingAllCharacters_returnsTotalCharactersSkippedAndReachesEnd() throws IOException {
        // Arrange
        String inputSequence = "A test sequence with several characters.";
        CharSequenceReader reader = new CharSequenceReader(inputSequence);
        long totalCharacters = inputSequence.length();

        // Act
        long charactersSkipped = reader.skip(totalCharacters);

        // Assert
        assertEquals("The number of skipped characters should match the sequence length.",
                totalCharacters, charactersSkipped);
        assertEquals("The reader should be at the end of the sequence after skipping all characters.",
                -1, reader.read());
    }
}