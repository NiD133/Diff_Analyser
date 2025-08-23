package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the utility methods in {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Verifies that the isWhitespace method correctly identifies the
     * horizontal tab character as whitespace.
     */
    @Test
    public void isWhitespaceReturnsTrueForTabCharacter() {
        // The original test used the integer 9, which is the code point for a horizontal tab.
        // Using the character literal '\t' makes the test's intent much clearer.
        assertTrue(
            "The tab character should be considered whitespace.",
            StringUtil.isWhitespace('\t')
        );
    }
}