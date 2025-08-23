package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil#isHexDigit(char)} method.
 */
public class StringUtilTest {

    @Test
    public void isHexDigit_shouldReturnTrue_forUppercaseHexLetters() {
        assertTrue("Character 'A' should be a valid hex digit", StringUtil.isHexDigit('A'));
        assertTrue("Character 'F' should be a valid hex digit", StringUtil.isHexDigit('F'));
    }

    @Test
    public void isHexDigit_shouldReturnTrue_forLowercaseHexLetters() {
        assertTrue("Character 'a' should be a valid hex digit", StringUtil.isHexDigit('a'));
        assertTrue("Character 'f' should be a valid hex digit", StringUtil.isHexDigit('f'));
    }

    @Test
    public void isHexDigit_shouldReturnTrue_forDigits() {
        assertTrue("Character '0' should be a valid hex digit", StringUtil.isHexDigit('0'));
        assertTrue("Character '9' should be a valid hex digit", StringUtil.isHexDigit('9'));
    }

    @Test
    public void isHexDigit_shouldReturnFalse_forNonHexCharacters() {
        assertFalse("Character 'G' is not a hex digit", StringUtil.isHexDigit('G'));
        assertFalse("Character 'z' is not a hex digit", StringUtil.isHexDigit('z'));
        assertFalse("Symbol '$' is not a hex digit", StringUtil.isHexDigit('$'));
        assertFalse("Space character is not a hex digit", StringUtil.isHexDigit(' '));
    }
}