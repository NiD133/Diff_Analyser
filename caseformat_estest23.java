package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Tests that the internal `convert` method normalizes a string even when
     * converting from a format to itself. For {@code LOWER_HYPHEN}, this means
     * converting the entire input string to lowercase.
     *
     * <p>Note: The public {@code to(CaseFormat, String)} method short-circuits
     * and returns the original string for identity conversions. This test targets the
     * package-private {@code convert} method directly to verify its normalization logic.
     */
    @Test
    public void convert_fromLowerHyphenToItself_normalizesInputToLowerCase() {
        // Arrange
        CaseFormat format = CaseFormat.LOWER_HYPHEN;
        String mixedCaseInput = "83pvzR?h!";
        String expectedLowercaseOutput = "83pvzr?h!";

        // Act
        // Calling the internal convert method directly.
        String actualOutput = format.convert(format, mixedCaseInput);

        // Assert
        assertEquals(
            "Converting from a format to itself should normalize the string to that format's standard.",
            expectedLowercaseOutput,
            actualOutput);
    }
}