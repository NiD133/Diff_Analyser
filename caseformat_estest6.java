package com.google.common.base;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void valueOf_givenValidName_returnsCorrespondingEnum() {
        // Arrange
        String lowerCamelName = "LOWER_CAMEL";
        CaseFormat expectedFormat = CaseFormat.LOWER_CAMEL;

        // Act
        CaseFormat actualFormat = CaseFormat.valueOf(lowerCamelName);

        // Assert
        assertSame("valueOf should return the correct enum constant for a valid name.",
                expectedFormat, actualFormat);
    }
}