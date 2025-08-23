package com.google.common.base;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void normalizeFirstWord_forUpperCamelWithEmptyString_returnsEmptyString() {
        // Arrange
        String emptyInput = "";
        String expectedOutput = "";

        // Act
        String result = CaseFormat.UPPER_CAMEL.normalizeFirstWord(emptyInput);

        // Assert
        assertEquals(expectedOutput, result);
    }
}