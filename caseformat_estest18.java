package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Provides unit tests for the {@link CaseFormat} class.
 */
public class CaseFormatTest {

    @Test
    public void to_fromLowerUnderscoreToUperUnderscore_handlesNonStandardInput() {
        // This test verifies the "best-effort" conversion for an input string
        // that does not strictly conform to the LOWER_UNDERSCORE format.
        // The input contains a mix of uppercase, lowercase, and special characters.
        // The expected behavior is that the string is still converted by uppercasing
        // the parts delimited by the underscore.

        // Arrange
        final CaseFormat sourceFormat = CaseFormat.LOWER_UNDERSCORE;
        final CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;
        final String nonStandardInput = "Q#&'.zTN&p_";
        final String expectedOutput = "Q#&'.ZTN&P_";

        // Act
        final String actualOutput = sourceFormat.to(targetFormat, nonStandardInput);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}