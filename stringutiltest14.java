package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link StringUtil#isDigit(char)}.
 */
class StringUtilTest {

    @DisplayName("isDigit should return true for ASCII digits '0' through '9'")
    @ParameterizedTest(name = "for character ''{0}''")
    @ValueSource(chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'})
    void isDigit_returnsTrue_forAsciiDigits(char digit) {
        assertTrue(StringUtil.isDigit(digit));
    }

    @DisplayName("isDigit should return false for non-digit characters")
    @ParameterizedTest(name = "for character ''{0}''")
    @ValueSource(chars = {'a', 'A', 'ä', 'Ä', '١', '୳', ' ', '#', '.'})
    void isDigit_returnsFalse_forNonDigitCharacters(char nonDigit) {
        assertFalse(StringUtil.isDigit(nonDigit));
    }
}