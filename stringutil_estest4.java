package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the StringUtil#isHexDigit(char) method.
 */
public class StringUtilTest {

    @Test
    public void isHexDigit_shouldReturnTrue_forLowerCaseHexCharacter() {
        // The character 'a' is a valid hexadecimal digit.
        assertTrue("'a' should be recognized as a hex digit", StringUtil.isHexDigit('a'));
    }

    @Test
    public void isHexDigit_shouldReturnTrue_forUpperCaseHexCharacter() {
        // The character 'F' is a valid hexadecimal digit.
        assertTrue("'F' should be recognized as a hex digit", StringUtil.isHexDigit('F'));
    }

    @Test
    public void isHexDigit_shouldReturnTrue_forNumericDigit() {
        // The character '5' is a valid hexadecimal digit.
        assertTrue("'5' should be recognized as a hex digit", StringUtil.isHexDigit('5'));
    }

    @Test
    public void isHexDigit_shouldReturnFalse_forNonHexCharacter() {
        // The character 'g' is not a valid hexadecimal digit.
        assertFalse("'g' should not be recognized as a hex digit", StringUtil.isHexDigit('g'));
    }

    @Test
    public void isHexDigit_shouldReturnFalse_forSymbol() {
        // The character '$' is not a valid hexadecimal digit.
        assertFalse("'$' should not be recognized as a hex digit", StringUtil.isHexDigit('$'));
    }
}