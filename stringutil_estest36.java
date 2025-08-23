package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isDigitShouldReturnTrueForDigitCharacters() {
        // Test the full range of ASCII digits
        assertTrue("'0' should be considered a digit", StringUtil.isDigit('0'));
        assertTrue("'5' should be considered a digit", StringUtil.isDigit('5'));
        assertTrue("'9' should be considered a digit", StringUtil.isDigit('9'));
    }

    @Test
    public void isDigitShouldReturnFalseForNonDigitCharacters() {
        // Test various non-digit characters
        assertFalse("A lowercase letter 'a' is not a digit", StringUtil.isDigit('a'));
        assertFalse("An uppercase letter 'Z' is not a digit", StringUtil.isDigit('Z'));
        assertFalse("A space character is not a digit", StringUtil.isDigit(' '));
        assertFalse("A symbol character '$' is not a digit", StringUtil.isDigit('$'));
    }
}