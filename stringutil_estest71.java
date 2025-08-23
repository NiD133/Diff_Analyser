package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isActuallyWhitespaceReturnsTrueForLineFeed() {
        // The integer 10 is the ASCII code point for the Line Feed character ('\n').
        // This test verifies that the method correctly identifies it as whitespace.
        assertTrue("Line Feed character should be considered whitespace", StringUtil.isActuallyWhitespace('\n'));
    }
}