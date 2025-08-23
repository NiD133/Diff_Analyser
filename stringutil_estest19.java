package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link StringUtil}.
 */
public class StringUtilTest {

    @Test
    public void paddingWithZeroWidthReturnsEmptyString() {
        // Arrange
        final int width = 0;
        final String expectedPadding = "";

        // Act
        final String actualPadding = StringUtil.padding(width);

        // Assert
        assertEquals(expectedPadding, actualPadding);
    }
}