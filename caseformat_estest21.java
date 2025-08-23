package com.google.common.base;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test(expected = NullPointerException.class)
    public void to_whenGivenNullInputString_throwsNullPointerException() {
        // Arrange
        CaseFormat sourceFormat = CaseFormat.LOWER_HYPHEN;
        CaseFormat targetFormat = CaseFormat.UPPER_UNDERSCORE;

        // Act
        // The 'to' method should throw a NullPointerException when the input string is null.
        sourceFormat.to(targetFormat, null);

        // Assert (handled by the @Test(expected=...) annotation)
    }
}