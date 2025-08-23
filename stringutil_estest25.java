package org.jsoup.internal;

import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the StringUtil class.
 */
public class StringUtilTest {

    /**
     * Verifies that StringUtil.padding() correctly generates a string of spaces
     * for a width that is larger than its internal cache, which has a size of 20.
     */
    @Test
    public void paddingWithWidthGreaterThanCacheReturnsCorrectString() {
        // Arrange: Define a width larger than the internal cache size.
        int largeWidth = 100;
        
        // Create the expected string of 100 spaces for comparison.
        char[] expectedChars = new char[largeWidth];
        Arrays.fill(expectedChars, ' ');
        String expectedPadding = new String(expectedChars);

        // Act: Call the padding method with a large width and an unlimited max size.
        String actualPadding = StringUtil.padding(largeWidth, -1);

        // Assert: Verify that the generated string has the correct length and content.
        assertEquals(
            "The generated padding string should have the correct length.",
            largeWidth, 
            actualPadding.length()
        );
        assertEquals(
            "The generated string should consist entirely of spaces.",
            expectedPadding, 
            actualPadding
        );
    }
}