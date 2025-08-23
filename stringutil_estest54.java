package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link StringUtil#isAsciiLetter(char)} method.
 */
public class StringUtilTest {

    @Test
    public void isAsciiLetter_shouldReturnTrue_forUppercaseLetters() {
        assertTrue("An uppercase character like 'A' should be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('A'));
        assertTrue("An uppercase character like 'Z' should be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('Z'));
    }

    @Test
    public void isAsciiLetter_shouldReturnTrue_forLowercaseLetters() {
        assertTrue("A lowercase character like 'a' should be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('a'));
        assertTrue("A lowercase character like 'z' should be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('z'));
    }

    @Test
    public void isAsciiLetter_shouldReturnFalse_forNonAsciiLetters() {
        assertFalse("A digit like '7' should not be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('7'));
        assertFalse("A symbol like '$' should not be identified as an ASCII letter.",
            StringUtil.isAsciiLetter('$'));
        assertFalse("A whitespace character should not be identified as an ASCII letter.",
            StringUtil.isAsciiLetter(' '));
    }

    @Test
    public void isAsciiLetter_shouldReturnFalse_forBoundaryCharacters() {
        // Test characters with ASCII values immediately adjacent to the letter ranges.
        assertFalse("The character before 'A' should not be an ASCII letter.",
            StringUtil.isAsciiLetter('@'));
        assertFalse("The character after 'Z' should not be an ASCII letter.",
            StringUtil.isAsciiLetter('['));
        assertFalse("The character before 'a' should not be an ASCII letter.",
            StringUtil.isAsciiLetter('`'));
        assertFalse("The character after 'z' should not be an ASCII letter.",
            StringUtil.isAsciiLetter('{'));
    }
}