package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the StringUtil class, focusing on the appendNormalisedWhitespace method.
 */
public class StringUtilTest {

    @Test
    public void appendNormalisedWhitespaceStripsAllWhitespaceWhenInputIsOnlyWhitespace() {
        // Arrange
        StringBuilder accumulator = new StringBuilder();
        String inputWithOnlyWhitespace = "         "; // Input consists of multiple space characters.

        // Act
        // Call the method with stripLeading=true, which should treat the entire string as leading whitespace.
        StringUtil.appendNormalisedWhitespace(accumulator, inputWithOnlyWhitespace, true);

        // Assert
        // The accumulator should be empty because the input string was entirely stripped.
        assertEquals("The accumulator should be empty.", "", accumulator.toString());
    }
}