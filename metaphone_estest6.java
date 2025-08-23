package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    /**
     * Tests that the metaphone algorithm correctly handles a string that includes
     * leading non-alphabetic characters (numbers and symbols). The Javadoc for
     * Metaphone states that the input should only be A-Z, so this test verifies
     * that the implementation is robust enough to ignore such characters and
     * process the rest of the string.
     */
    @Test
    public void metaphoneShouldIgnoreLeadingNonAlphabeticCharacters() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String inputWithNonAlphabeticChars = "-15bgQcY";
        final String expectedEncoding = "BKS";

        // Act
        final String actualEncoding = metaphone.metaphone(inputWithNonAlphabeticChars);

        // Assert
        assertEquals("The Metaphone encoding should ignore leading non-alphabetic characters",
                     expectedEncoding, actualEncoding);
    }
}