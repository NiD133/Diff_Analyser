package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the Metaphone algorithm correctly encodes the string "SCH" to "SK".
     * This is a specific encoding rule for Metaphone.
     */
    @Test
    public void metaphoneShouldEncodeSchAsSk() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String input = "SCH";
        String expectedCode = "SK";

        // Act
        String actualCode = metaphone.metaphone(input);

        // Assert
        assertEquals("The metaphone code for 'SCH' should be 'SK'", expectedCode, actualCode);
    }

    /**
     * Tests that a new Metaphone instance is created with the default max code length of 4.
     */
    @Test
    public void constructorShouldSetDefaultMaxCodeLength() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        int expectedMaxCodeLen = 4;

        // Act
        int actualMaxCodeLen = metaphone.getMaxCodeLen();

        // Assert
        assertEquals("Default max code length should be 4", expectedMaxCodeLen, actualMaxCodeLen);
    }
}