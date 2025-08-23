package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that normaliseWhitespace returns an empty string when the input is empty.
     * This confirms the method correctly handles the edge case of an empty input string.
     */
    @Test
    public void normaliseWhitespaceShouldReturnEmptyStringForEmptyInput() {
        // Arrange
        String input = "";
        String expected = "";

        // Act
        String actual = StringUtil.normaliseWhitespace(input);

        // Assert
        assertEquals("Normalizing whitespace on an empty string should result in an empty string.", expected, actual);
    }
}