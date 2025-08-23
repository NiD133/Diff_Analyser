package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void isActuallyWhitespaceReturnsFalseForNegativeCodePoint() {
        // Arrange: A negative integer is not a valid Unicode code point.
        int invalidCodePoint = -1;

        // Act: Check if the invalid code point is considered whitespace.
        boolean isWhitespace = StringUtil.isActuallyWhitespace(invalidCodePoint);

        // Assert: The result should be false.
        assertFalse("A negative code point should not be considered whitespace", isWhitespace);
    }
}