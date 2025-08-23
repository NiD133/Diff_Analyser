package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the StringUtil class.
 */
public class StringUtilTest {

    /**
     * Verifies that isHexDigit() correctly identifies numeric characters as valid hex digits.
     */
    @Test
    public void isHexDigitShouldReturnTrueForNumericDigit() {
        // The character '9' is a valid hexadecimal digit (0-9, a-f, A-F).
        // This test confirms that numeric digits are correctly recognized.
        assertTrue(
            "The character '9' should be recognized as a valid hex digit",
            StringUtil.isHexDigit('9')
        );
    }
}