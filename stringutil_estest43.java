package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that the isWhitespace method correctly identifies the space character.
     */
    @Test
    public void isWhitespaceShouldReturnTrueForSpaceCharacter() {
        // The integer value 32 represents the code point for the space character (' ').
        // The test confirms that this common whitespace character is correctly identified.
        assertTrue("The space character should be classified as whitespace.", StringUtil.isWhitespace(' '));
    }
}