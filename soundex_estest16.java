package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Soundex} class, focusing on specific edge cases.
 */
public class SoundexTest {

    /**
     * Tests that the difference score is 0 when comparing a string that contains
     * no letters against another string.
     * <p>
     * The Soundex algorithm ignores non-alphabetic characters. A string without any
     * letters will produce an empty Soundex code. The difference utility considers
     * a comparison involving an empty code to have a difference of 0.
     * </p>
     */
    @Test
    public void testDifferenceIsZeroWhenOneStringContainsNoLetters() throws EncoderException {
        // Arrange
        final Soundex soundex = new Soundex();
        // This string contains no letters and will result in an empty Soundex code.
        final String stringWithoutLetters = "6]5]'=[=9";
        // A standard string for comparison.
        final String standardString = "Testing";
        final int expectedDifference = 0;

        // Act
        final int actualDifference = soundex.difference(stringWithoutLetters, standardString);

        // Assert
        assertEquals(expectedDifference, actualDifference);
    }
}