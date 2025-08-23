package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test for the StringUtil#appendNormalisedWhitespace method.
 */
public class StringUtilTest {

    /**
     * Verifies that a string containing multiple consecutive spaces is normalized to a single space
     * when appended to a StringBuilder, with the 'stripLeading' option disabled.
     */
    @Test
    public void appendNormalisedWhitespaceShouldCollapseMultipleSpacesToOne() {
        // Arrange
        StringBuilder stringBuilder = new StringBuilder();
        String inputWithMultipleSpaces = "    "; // A shorter, readable example is sufficient.
        String expected = " ";

        // Act
        StringUtil.appendNormalisedWhitespace(stringBuilder, inputWithMultipleSpaces, false);

        // Assert
        assertEquals(expected, stringBuilder.toString());
    }
}