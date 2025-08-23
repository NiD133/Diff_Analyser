package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class, focusing on specific encoding rules and default configuration.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone encoding for "SCi" is "X".
     * This case covers the specific rule where "SC" at the beginning of a word,
     * when followed by a front vowel ('I', 'E', 'Y'), is encoded as 'X'.
     */
    @Test
    public void metaphoneShouldEncodeInitialScFollowedByFrontVowelAsX() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "SCi";
        final String expectedEncoding = "X";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("Encoding 'SCi' should result in 'X'", expectedEncoding, actualEncoding);
    }

    /**
     * Tests that a new Metaphone instance is configured with the default
     * maximum code length of 4.
     */
    @Test
    public void newMetaphoneInstanceShouldHaveDefaultMaxCodeLength() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLen = 4;

        // Act
        final int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}