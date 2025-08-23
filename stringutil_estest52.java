package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isAsciiLetterShouldReturnFalseForNonLetterCharacter() {
        // The '!' character is a symbol, not an ASCII letter in the a-z or A-Z range.
        assertFalse(
            "isAsciiLetter() should return false for a non-letter character.",
            StringUtil.isAsciiLetter('!')
        );
    }
}