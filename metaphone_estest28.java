package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone encoding for "GA" is "K".
     * This specific case verifies the rule where an initial 'G' followed by 'A'
     * is encoded as 'K'.
     */
    @Test
    public void shouldEncodeGaAsK() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "GA";
        final String expectedCode = "K";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone code for 'GA' should be 'K'", expectedCode, actualCode);
    }

    /**
     * Tests that a new Metaphone instance is initialized with the default
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
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}