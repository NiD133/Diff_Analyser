package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Provides understandable and maintainable tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that a new Metaphone instance is correctly initialized with the
     * default maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        int expectedMaxCodeLength = 4;

        // Act
        int actualMaxCodeLength = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLength, actualMaxCodeLength);
    }

    /**
     * Tests the encoding of a simple string "GtA" to verify several basic
     * Metaphone rules are applied correctly.
     */
    @Test
    public void shouldEncodeSimpleStringApplyingBasicRules() {
        // This test case verifies the following Metaphone rules for the input "GtA":
        // 1. Input is uppercased to "GTA".
        // 2. Initial 'G' (not followed by 'H') is encoded as 'K'.
        // 3. 'T' (not part of a special sequence like "TH") is encoded as 'T'.
        // 4. The vowel 'A' is dropped because it is not the first letter.
        // The expected result is "KT".

        // Arrange
        Metaphone metaphone = new Metaphone();
        String input = "GtA";
        String expectedEncoding = "KT";

        // Act
        String actualEncoding = metaphone.encode(input);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }
}