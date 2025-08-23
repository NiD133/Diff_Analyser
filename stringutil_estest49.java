package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the StringUtil class, focusing on character-checking methods.
 */
public class StringUtilTest {

    @Test
    public void isHexDigitShouldReturnFalseForNonHexCharacter() {
        // The isHexDigit method should return false for characters that are not
        // in the ranges '0'-'9', 'a'-'f', or 'A'-'F'.
        // The double-quote character ('"') is used as a representative non-hexadecimal symbol.
        assertFalse("A double-quote character should not be classified as a hex digit.",
            StringUtil.isHexDigit('\"'));
    }
}