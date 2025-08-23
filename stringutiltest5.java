package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link StringUtil#isNumeric(String)} method.
 */
@DisplayName("StringUtil.isNumeric")
class StringUtilTest {

    @DisplayName("should return true for strings containing only ASCII digits")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @ValueSource(strings = {"1", "1234", "0"})
    void isNumeric_returnsTrue_forValidNumericStrings(String numericInput) {
        assertTrue(StringUtil.isNumeric(numericInput));
    }

    @DisplayName("should return false for non-numeric or invalid inputs")
    @ParameterizedTest(name = "Input: \"{0}\"")
    @NullAndEmptySource // Covers null and "" cases
    @ValueSource(strings = {
        " ",           // Whitespace
        "123 546",     // Contains a space
        "hello",       // Contains letters
        "123.334",     // Contains a decimal point
        "-1"           // Contains a sign character
    })
    void isNumeric_returnsFalse_forInvalidOrNonNumericStrings(String nonNumericInput) {
        // The method's contract is to return true only for strings containing exclusively ASCII digits.
        // Any other character, including spaces, signs, or decimal points, makes it non-numeric.
        assertFalse(StringUtil.isNumeric(nonNumericInput));
    }
}