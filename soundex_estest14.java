package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link org.apache.commons.codec.language.Soundex} class.
 */
public class SoundexTest {

    /**
     * Tests that the 'difference' method returns the maximum score (4) for two identical strings.
     * This test uses an input string containing non-alphabetic characters to ensure they are
     * correctly handled and ignored by the algorithm.
     */
    @Test
    public void differenceOfIdenticalStringsReturnsMaximumScore() throws Exception {
        // Arrange
        final Soundex soundex = new Soundex();
        final String testString = "Hr~hEi";
        final int expectedScore = 4; // The maximum possible similarity score.

        // Act
        final int actualScore = soundex.difference(testString, testString);

        // Assert
        assertEquals("The difference score for two identical strings should be the maximum value of 4.",
                     expectedScore, actualScore);
    }
}