package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    @Test
    public void paddingWithZeroWidthShouldReturnEmptyString() {
        // Arrange: Define the input parameters for the test.
        // A requested padding width of 0 should always result in an empty string.
        // The maxPaddingWidth of -1 signifies unlimited padding, which is a valid but
        // irrelevant parameter when the requested width is zero.
        int requestedWidth = 0;
        int maxPaddingWidth = -1;

        // Act: Call the method under test.
        String actualPadding = StringUtil.padding(requestedWidth, maxPaddingWidth);

        // Assert: Verify the result is an empty string.
        assertEquals("", actualPadding);
    }
}