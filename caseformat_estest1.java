package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CaseFormat}.
 *
 * Note: The original test was auto-generated and has been simplified for clarity.
 */
public class CaseFormatTest {

    @Test
    public void to_fromUpperUnderscoreToLowerCamel_withNonStandardInput_simplyLowercases() {
        // Arrange
        // The "best effort" conversion is tested with an input string that does not
        // strictly conform to the UPPER_UNDERSCORE format. It includes mixed case,
        // numbers, and various symbols.
        final String nonConformingInput = "Y3:E]9bSR@H%B/_?";
        final CaseFormat sourceFormat = CaseFormat.UPPER_UNDERSCORE;
        final CaseFormat targetFormat = CaseFormat.LOWER_CAMEL;

        // Act
        final String result = sourceFormat.to(targetFormat, nonConformingInput);

        // Assert
        // The expected behavior for this specific non-conforming input is that
        // the conversion logic effectively just lower-cases the original string.
        final String expectedOutput = "y3:e]9bsr@h%b/?";
        assertEquals(expectedOutput, result);
    }
}