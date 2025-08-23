package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that the isWhitespace() method correctly identifies the
     * Line Feed character (code point 10) as whitespace, per the HTML specification.
     */
    @Test
    public void isWhitespaceShouldReturnTrueForLineFeed() {
        // The original test used the integer literal 10. Using the character literal '\n'
        // is functionally equivalent but makes the test's intent immediately clear.
        // The char is implicitly converted to its integer code point (10) when passed to the method.
        
        assertTrue("The Line Feed character '\\n' should be classified as whitespace.",
            StringUtil.isWhitespace('\n'));
    }
}