package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    /**
     * Verifies that converting a string from a format to the very same format
     * returns the original string unchanged. This is an identity conversion.
     */
    @Test
    public void to_shouldReturnUnchangedString_whenConvertingToSameFormat() {
        // Arrange
        CaseFormat format = CaseFormat.UPPER_UNDERSCORE;
        String originalString = " ALUEZ";

        // Act
        String result = format.to(format, originalString);

        // Assert
        assertEquals(originalString, result);
    }
}