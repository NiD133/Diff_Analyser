package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link Metaphone} class, focusing on its encoding behavior.
 */
public class MetaphoneTest {

    @Test
    public void encodeShouldIgnoreNonAlphabeticCharacters() {
        // Arrange
        Metaphone metaphone = new Metaphone();
        // The input string contains punctuation, symbols, and numbers,
        // which the Metaphone algorithm should ignore. The effective input becomes "TBFw".
        String inputWithNonLetters = "T(b`F_8$w";

        // The expected Metaphone code for "TBFw" is "TBF".
        // T -> T
        // B -> B
        // F -> F
        // W -> Dropped at the end of a word unless it follows a vowel.
        String expectedCode = "TBF";

        // Act
        String actualCode = metaphone.encode(inputWithNonLetters);

        // Assert
        assertEquals("The encoded string should correctly handle non-alphabetic characters.",
                     expectedCode, actualCode);
        
        // The original test also verified the default max code length.
        // This confirms that the encoding process does not alter the object's configuration.
        assertEquals("The default max code length should remain 4 after encoding.",
                     4, metaphone.getMaxCodeLen());
    }
}