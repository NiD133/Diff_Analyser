package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that isAsciiLetter returns true for a standard lowercase ASCII letter.
     */
    @Test
    public void isAsciiLetter_shouldReturnTrue_forLowercaseLetter() {
        assertTrue("'a' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('a'));
    }

    /**
     * Verifies that isAsciiLetter returns true for a standard uppercase ASCII letter.
     */
    @Test
    public void isAsciiLetter_shouldReturnTrue_forUppercaseLetter() {
        assertTrue("'Z' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('Z'));
    }

    /**
     * Verifies that isAsciiLetter returns false for a character that is not a letter.
     */
    @Test
    public void isAsciiLetter_shouldReturnFalse_forNonLetterCharacter() {
        assertFalse("'$' should not be recognized as an ASCII letter", StringUtil.isAsciiLetter('$'));
    }

    /**
     * Verifies that isAsciiLetter returns false for a digit.
     */
    @Test
    public void isAsciiLetter_shouldReturnFalse_forDigit() {
        assertFalse("'7' should not be recognized as an ASCII letter", StringUtil.isAsciiLetter('7'));
    }
}