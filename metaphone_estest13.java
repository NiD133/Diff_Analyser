package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    @Test
    public void metaphoneShouldCorrectlyEncodeStringWithMixedCaseAndNonAlphabeticCharacters() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        String complexInput = "dgioM<zN0.Q`{R [";
        String expectedCode = "JMSN";

        /*
         * This test case verifies how the Metaphone algorithm handles a complex string.
         * The process is as follows:
         *
         * 1. Sanitize Input: The input string is first converted to uppercase and all
         *    non-alphabetic characters are removed, resulting in "DGIOMZNR".
         *
         * 2. Apply Metaphone Rules:
         *    - "DGI" is a special case that encodes to "J".
         *    - "O" is a vowel that appears after the first letter, so it is dropped.
         *    - "M" encodes to "M".
         *    - "Z" encodes to "S".
         *    - "N" encodes to "N".
         *
         * 3. Final Code: The resulting code is "JMSN", which reaches the default
         *    maximum length of 4 characters.
         */

        // Act
        String actualCode = metaphone.metaphone(complexInput);

        // Assert
        assertEquals("The metaphone code for a complex string was not generated correctly.",
                     expectedCode, actualCode);
    }
}