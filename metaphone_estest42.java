package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class, focusing on its encoding behavior and
 * default configuration.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone encoding for a simple string of consonants, which do not
     * trigger any special encoding rules, remains unchanged.
     */
    @Test
    public void testMetaphoneWithSimpleConsonantString() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String input = "KBMF";
        final String expectedCode = "KBMF";

        // Act
        final String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The Metaphone code for a simple consonant string should be unchanged.",
                expectedCode, actualCode);
    }

    /**
     * Verifies that a new instance of the Metaphone encoder has a default
     * maximum code length of 4, as specified by the algorithm.
     */
    @Test
    public void testDefaultMaxCodeLengthIsFour() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final int expectedLength = 4;

        // Act
        final int actualLength = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("The default max code length should be 4.",
                expectedLength, actualLength);
    }
}