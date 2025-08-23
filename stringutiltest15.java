package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for {@link StringUtil}.
 * This focuses on the {@link StringUtil#isHexDigit(char)} method.
 */
@DisplayName("StringUtil.isHexDigit")
public class StringUtilTest {

    private static Stream<Character> validHexCharactersProvider() {
        return "0123456789abcdefABCDEF".chars().mapToObj(c -> (char) c);
    }

    @ParameterizedTest(name = "should return true for valid hex character ''{0}''")
    @MethodSource("validHexCharactersProvider")
    void shouldReturnTrueForValidHexCharacters(char hexChar) {
        assertTrue(StringUtil.isHexDigit(hexChar));
    }

    @ParameterizedTest(name = "should return false for non-hex character ''{0}''")
    @ValueSource(chars = {
        'g', 'G', 'z', // Letters outside a-f range
        'ä', 'Ä',     // Non-ASCII letters
        ' ', '#', '$', // Symbols and whitespace
        '١',          // ARABIC-INDIC DIGIT ONE
        '୳'           // ORIYA FRACTION ONE QUARTER
    })
    void shouldReturnFalseForNonHexCharacters(char nonHexChar) {
        assertFalse(StringUtil.isHexDigit(nonHexChar));
    }
}