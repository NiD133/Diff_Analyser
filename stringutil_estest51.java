package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link StringUtil#isDigit(char)} method.
 */
public class StringUtilTest {

    @Test
    public void isDigit_shouldReturnFalse_forNonDigitCharacter() {
        // Arrange: A character that is not a numerical digit.
        char nonDigitChar = '%';

        // Act: Check if the character is a digit.
        boolean result = StringUtil.isDigit(nonDigitChar);

        // Assert: The method should return false.
        assertFalse("Expected isDigit('%') to be false, as '%' is not a digit.", result);
    }
}