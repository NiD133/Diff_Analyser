package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the internal StringUtil class, focusing on the static padding array.
 */
public class StringUtilTest {

    @Test
    @DisplayName("The memoized padding array should be correctly initialized")
    void memoizedPaddingArrayIsCorrectlyInitialized() {
        // The StringUtil.padding array is a static, final, pre-calculated cache
        // for strings of spaces. This test verifies its integrity upon initialization.

        // Arrange: The array is a static final field, so no setup is needed.
        // The source code indicates the array holds padding for lengths 0 through 20.
        String[] padding = StringUtil.padding;
        int expectedSize = 21;

        // Assert: Verify the size of the cache.
        assertEquals(expectedSize, padding.length,
            "The padding array should contain " + expectedSize + " elements (for lengths 0-20).");

        // Assert: Verify that each element in the array has the correct length.
        for (int i = 0; i < padding.length; i++) {
            assertEquals(i, padding[i].length(),
                "The padding string at index " + i + " should have a length of " + i + ".");
        }
    }
}