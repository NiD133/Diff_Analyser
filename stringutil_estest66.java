package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that appendNormalisedWhitespace does not alter a string that
     * already contains normalised whitespace, even when stripLeading is true.
     */
    @Test
    public void appendNormalisedWhitespaceOnAlreadyNormalisedStringIsUnchanged() {
        // Arrange
        StringBuilder accumulator = StringUtil.borrowBuilder();
        String inputString = "width must be >= 0";
        // The input string has no leading, trailing, or consecutive whitespace.
        // Therefore, it should remain unchanged.
        String expected = "width must be >= 0";

        // Act
        StringUtil.appendNormalisedWhitespace(accumulator, inputString, true);

        // Assert
        assertEquals(expected, accumulator.toString());
    }
}