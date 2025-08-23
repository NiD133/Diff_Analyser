package com.google.common.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link CaseFormat}.
 */
public class CaseFormatTest {

    @Test
    public void to_fromLowerCamelToLoweUnderscore_withNonStandardInput_convertsAsBestEffort() {
        // The `to` method documentation states a "best effort" approach is taken
        // for strings that do not strictly conform to the source format. This test
        // verifies that conversion works reasonably with a mix of numbers, symbols,
        // and mixed-case letters, inserting an underscore before each capital letter.

        // Arrange
        final String nonStandardLowerCamel = "0P|{HG$S{ax$v|r_  ";
        final String expectedLowerUnderscore = "0_p|{_h_g$_s{ax$v|r_  ";

        // Act
        final String result = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, nonStandardLowerCamel);

        // Assert
        assertEquals(expectedLowerUnderscore, result);
    }
}