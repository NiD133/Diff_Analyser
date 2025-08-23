package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that a non-whitespace character is correctly identified as such.
     */
    @Test
    public void isActuallyWhitespaceReturnsFalseForNonWhitespaceCharacter() {
        // The isActuallyWhitespace() method defines whitespace as standard ASCII spaces
        // (space, tab, newline, etc.) and the non-breaking space (NBSP).
        // This test uses the character 'Á' (codepoint 193) as an example of a
        // character that should not be considered whitespace.

        boolean result = StringUtil.isActuallyWhitespace('Á');

        assertFalse("The character 'Á' should not be classified as whitespace.", result);
    }
}