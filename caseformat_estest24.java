package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Contains tests for the {@link CaseFormat} enum.
 */
public class CaseFormatTest {

    /**
     * Tests that {@code CaseFormat.UPPER_CAMEL.normalizeFirstWord} correctly handles a string
     * that starts with a non-alphabetic character.
     *
     * <p>For {@code UPPER_CAMEL}, {@code normalizeFirstWord} is implemented to capitalize the first
     * character and lowercase the rest. This test verifies that when the first character is not a
     * letter (e.g., a symbol), it remains unchanged, while the rest of the string is correctly
     * converted to lowercase.
     */
    @Test
    public void normalizeFirstWord_forUpperCamelWithNonAlphabeticFirstChar_lowercasesRestOfString() {
        // Arrange
        CaseFormat upperCamelFormat = CaseFormat.UPPER_CAMEL;
        String inputWithSymbolsAndMixedCase = "&/>Ql\"@^2R";
        String expectedOutput = "&/>ql\"@^2r";

        // Act
        String normalizedWord = upperCamelFormat.normalizeFirstWord(inputWithSymbolsAndMixedCase);

        // Assert
        assertEquals(
            "The method should leave the non-alphabetic first character as-is and lowercase the rest.",
            expectedOutput,
            normalizedWord);
    }
}