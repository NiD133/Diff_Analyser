package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void to_fromUpperUnderscoreToLowerUnderscore_withNonConformingLowercaseString_returnsUnchangedString() {
        // Arrange
        // This input string does not conform to the UPPER_UNDERSCORE format because it's already
        // lowercase and contains special characters and spaces. The conversion logic should
        // handle this gracefully.
        String nonConformingInput = "2$ddmbvc\" rj0j %>[";
        String expectedOutput = "2$ddmbvc\" rj0j %>[";

        // Act
        // Attempt to convert from UPPER_UNDERSCORE to LOWER_UNDERSCORE.
        // The optimized path for this conversion simply lowercases the string. Since the
        // input contains no uppercase letters, it should remain unchanged.
        String actualOutput = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_UNDERSCORE, nonConformingInput);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}