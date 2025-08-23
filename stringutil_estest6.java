package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil#isAsciiLetter(char)} method.
 */
public class StringUtilTest {

    @Test
    public void isAsciiLetter_shouldReturnTrue_forUppercaseLetters() {
        // Test boundaries
        assertTrue("'A' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('A'));
        assertTrue("'Z' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('Z'));

        // Test a character in the middle of the range
        assertTrue("'M' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('M'));
    }

    @Test
    public void isAsciiLetter_shouldReturnTrue_forLowercaseLetters() {
        // Test boundaries
        assertTrue("'a' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('a'));
        assertTrue("'z' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('z'));

        // Test a character in the middle of the range
        assertTrue("'m' should be recognized as an ASCII letter", StringUtil.isAsciiLetter('m'));
    }

    @Test
    public void isAsciiLetter_shouldReturnFalse_forNonLetterCharacters() {
        // Test various non-letter characters
        assertFalse("A digit '5' should not be an ASCII letter", StringUtil.isAsciiLetter('5'));
        assertFalse("A symbol '$' should not be an ASCII letter", StringUtil.isAsciiLetter('$'));
        assertFalse("A space character should not be an ASCII letter", StringUtil.isAsciiLetter(' '));
        assertFalse("A non-ASCII character 'é' should not be an ASCII letter", StringUtil.isAsciiLetter('é'));
    }
}