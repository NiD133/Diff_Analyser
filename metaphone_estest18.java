package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Metaphone} class.
 */
public class MetaphoneTest {

    @Test
    public void shouldCorrectlyEncodeStringWithMixedCaseAndNonAlphabeticCharacters() {
        // Arrange
        final Metaphone metaphone = new Metaphone();
        final String inputWithMixedChars = "jL:A; nlq0j6l";
        final String expectedCode = "JLNL";

        // Act
        final String actualCode = metaphone.metaphone(inputWithMixedChars);

        // Assert
        assertEquals("Metaphone should ignore non-alphabetic characters and be case-insensitive.",
                     expectedCode,
                     actualCode);
    }
}