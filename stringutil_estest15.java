package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void isWhitespaceShouldReturnFalseForNulCharacter() {
        // The isWhitespace() method checks for characters considered whitespace in the HTML spec.
        // The NUL character (codepoint 0) is a control character, not HTML whitespace.
        
        int nulCodepoint = 0;
        
        assertFalse(
            "The NUL character (codepoint 0) should not be considered whitespace", 
            StringUtil.isWhitespace(nulCodepoint)
        );
    }
}