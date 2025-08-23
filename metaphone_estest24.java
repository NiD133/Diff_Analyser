package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Provides tests for the {@link Metaphone} class, focusing on its encoding logic
 * and default configuration.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes a simple string
     * consisting of consonants that have a direct mapping.
     */
    @Test
    public void shouldEncodeSimpleConsonantString() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "TVK";
        final String expectedEncoding = "TFK"; // 'T' -> 'T', 'V' -> 'F', 'K' -> 'K'

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone encoding for " + input + " is incorrect.", expectedEncoding, actualEncoding);
    }

    /**
     * Verifies that a new Metaphone instance is created with the default
     * maximum code length of 4.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("The default max code length should be " + expectedMaxCodeLen, expectedMaxCodeLen, actualMaxCodeLen);
    }
}