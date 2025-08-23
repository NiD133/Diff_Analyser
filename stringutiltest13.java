package org.jsoup.internal;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for the {@link StringUtil#isAsciiLetter(char)} method.
 */
// The class was renamed from StringUtilTestTest13 to the more conventional StringUtilTest.
public class StringUtilTest {

    @ParameterizedTest(name = "For char ''{0}'', should return true")
    @ValueSource(chars = {'a', 'n', 'z', 'A', 'N', 'Z'})
    void isAsciiLetter_shouldReturnTrue_forAsciiLetters(char letter) {
        assertTrue(StringUtil.isAsciiLetter(letter));
    }

    @ParameterizedTest(name = "For char ''{0}'', should return false")
    @ValueSource(chars = {' ', '-', '0', 'ß', 'Ě'})
    void isAsciiLetter_shouldReturnFalse_forNonAsciiLetters(char nonLetter) {
        assertFalse(StringUtil.isAsciiLetter(nonLetter));
    }
}