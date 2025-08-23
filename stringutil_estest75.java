package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the StringUtil class.
 */
public class StringUtilTest {

    @Test
    public void isWhitespace_shouldReturnTrue_forFormFeedCharacter() {
        // The HTML specification defines the Form Feed character (code point 12) as whitespace.
        // This test verifies that our utility method adheres to that definition.

        // Arrange
        int formFeedCodePoint = '\f'; // '\f' has the integer value 12

        // Act
        boolean result = StringUtil.isWhitespace(formFeedCodePoint);

        // Assert
        assertTrue("The Form Feed character should be considered whitespace.", result);
    }
}