package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Provides understandable and maintainable tests for the {@link Metaphone} class.
 * This refactors an auto-generated test to improve clarity and adhere to best practices.
 */
public class MetaphoneTest {

    /**
     * Tests that a new Metaphone instance is configured with the standard
     * maximum code length of 4.
     */
    @Test
    public void newInstanceShouldHaveDefaultMaxCodeLengthOfFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedMaxCodeLength = 4;

        // Act
        final int actualMaxCodeLength = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("A new Metaphone instance should default to a max code length of 4.",
                expectedMaxCodeLength, actualMaxCodeLength);
    }

    /**
     * Tests a simple encoding case where the single letter "v" should be
     * encoded as "V".
     */
    @Test
    public void shouldEncodeSingleLetterVAsV() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "v";
        final String expectedEncoding = "V";

        // Act
        final String actualEncoding = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone encoding of 'v' should be 'V'.",
                expectedEncoding, actualEncoding);
    }
}