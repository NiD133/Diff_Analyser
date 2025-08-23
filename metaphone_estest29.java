package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly handles a string containing
     * a mix of uppercase, lowercase, and non-alphabetic characters (numbers, symbols, spaces).
     * The implementation is expected to filter out non-alphabetic characters and
     * process the remaining letters in uppercase.
     */
    @Test
    public void metaphoneShouldEncodeStringWithNonAlphabeticCharacters() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        // The input string "!gm!DG77 KI}" should be sanitized to "GMDGKI" before encoding.
        String dirtyInput = "!gm!DG77 KI}";
        String expectedMetaphoneCode = "KMTK";

        // Act
        String actualMetaphoneCode = metaphone.metaphone(dirtyInput);

        // Assert
        assertEquals("The metaphone code should be correctly generated from a string with mixed characters.",
                     expectedMetaphoneCode, actualMetaphoneCode);
        
        // Also verify that the default max code length was used for the encoding.
        assertEquals("The default max code length should be 4.", 4, metaphone.getMaxCodeLen());
    }
}