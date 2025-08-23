package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the internal conversion logic of {@link CaseFormat}.
 */
public class CaseFormatUnderstandabilityTest {

    /**
     * Tests the behavior of the internal `convert` method when the source and target formats
     * are the same, and the input string does not conform to the source format.
     *
     * <p>The public {@link CaseFormat#to(CaseFormat, String)} method has an optimization
     * to return the input string directly if the source and target formats are identical.
     * This test bypasses that optimization by calling the package-private `convert` method,
     * which attempts a "best effort" conversion.
     *
     * <p>For a {@code LOWER_CAMEL} to {@code LOWER_CAMEL} conversion, with a non-conforming
     * input like "UPPER_UNDERSCORE", the observed behavior is that only the first character
     * is lower-cased. This test documents and verifies this specific, non-obvious behavior.
     */
    @Test
    public void convert_fromLowerCamelToLowerCamel_withNonConformingInput_lowercasesOnlyFirstChar() {
        // Arrange
        CaseFormat lowerCamelFormat = CaseFormat.LOWER_CAMEL;
        String nonConformingInput = "UPPER_UNDERSCORE";
        String expectedOutput = "uPPER_UNDERSCORE";

        // Act
        // We call the package-private convert() method directly to test its internal logic,
        // bypassing the shortcut in the public to() method.
        String actualOutput = lowerCamelFormat.convert(lowerCamelFormat, nonConformingInput);

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
}