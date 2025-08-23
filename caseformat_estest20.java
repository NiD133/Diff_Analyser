package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CaseFormat}.
 * This class focuses on specific or internal behaviors not covered by more general tests.
 */
public class CaseFormatTest {

    /**
     * This test verifies the behavior of the package-private `convert` method
     * when the source and target formats are the same.
     *
     * <p>Unlike the public `to()` method, which short-circuits and returns the
     * original string for self-conversion, the `convert` method still processes
     * and normalizes the input string according to the target format's rules.
     *
     * <p>This test ensures that for a `LOWER_UNDERSCORE` self-conversion,
     * an input string containing an uppercase letter is correctly normalized
     * to all lowercase.
     */
    @Test
    public void convert_fromLowerUnderscoreToItself_normalizesNonConformingInput() {
        // Arrange
        CaseFormat format = CaseFormat.LOWER_UNDERSCORE;
        // An input string that doesn't conform to LOWER_UNDERSCORE due to the uppercase 'R'.
        String nonConformingInput = "83pvzR?h!";
        String expectedNormalizedString = "83pvzr?h!";

        // Act
        // We are testing the package-private `convert` method directly to check its
        // normalization logic, which is bypassed by the public `to()` method in this case.
        String actualString = format.convert(format, nonConformingInput);

        // Assert
        assertEquals(expectedNormalizedString, actualString);
    }
}