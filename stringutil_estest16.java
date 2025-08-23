package org.jsoup.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Tests that the padding method returns a string of spaces of the correct length.
     * This specific test case uses a width of 21, which is just beyond the
     * internal cache size (0-20), ensuring the non-cached logic is exercised.
     */
    @Test
    public void paddingReturnsCorrectStringForWidthGreaterThanCache() {
        // Arrange
        final int width = 21;
        final String expectedPadding = new String(new char[width]).replace('\0', ' ');

        // Act
        final String actualPadding = StringUtil.padding(width, width);

        // Assert
        assertEquals(expectedPadding, actualPadding);
    }
}