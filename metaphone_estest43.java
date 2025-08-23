package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    private final Metaphone metaphone = new Metaphone();

    /**
     * Tests the Metaphone algorithm's special rule for words starting with "AE",
     * which should be encoded as "E".
     */
    @Test
    public void shouldEncodeWordStartingWithAEAsE() {
        // Arrange
        String input = "AEIOU";
        String expectedCode = "E";

        // Act
        String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The word 'AEIOU' should be encoded as 'E'", expectedCode, actualCode);
    }

    /**
     * Tests that a new Metaphone instance is created with the correct default
     * maximum code length.
     */
    @Test
    public void shouldHaveDefaultMaxCodeLenOfFour() {
        // Arrange
        int expectedMaxCodeLen = 4;

        // Act
        int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}