package org.jsoup.internal;

import org.junit.Test;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    /**
     * Tests that calling {@link StringUtil#padding(int, int)} with a negative
     * maxPaddingWidth (less than -1) throws an {@link IllegalArgumentException}.
     * The max padding width must be -1 (for unlimited) or a non-negative number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void paddingWithInvalidMaxWidthThrowsIllegalArgumentException() {
        // Arrange: Define a valid width and an invalid maximum padding width.
        final int width = 166;
        final int invalidMaxWidth = -37; // Must be >= -1

        // Act & Assert: Call the method with the invalid argument.
        // The @Test(expected) annotation asserts that an IllegalArgumentException is thrown.
        StringUtil.padding(width, invalidMaxWidth);
    }
}