package org.jsoup.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link StringUtil#padding(int)} and {@link StringUtil#padding(int, int)}.
 */
public class StringUtilTest {

    /**
     * Helper to create a string of spaces, making expected values in tests clearer.
     * @param count the number of spaces.
     * @return a string with the specified number of spaces.
     */
    private String spaces(int count) {
        return " ".repeat(Math.max(0, count));
    }

    @Nested
    @DisplayName("padding(int width)")
    class PaddingWithDefaultMaxWidth {
        private static final int DEFAULT_MAX_WIDTH = 30;

        @Test
        @DisplayName("Should return an empty string for zero width")
        void returnsEmptyStringForZeroWidth() {
            assertEquals("", StringUtil.padding(0));
        }

        @Test
        @DisplayName("Should return the correct number of spaces for widths within the default limit")
        void returnsCorrectPaddingForNormalWidths() {
            assertEquals(spaces(1), StringUtil.padding(1));
            assertEquals(spaces(15), StringUtil.padding(15));
            assertEquals(spaces(20), StringUtil.padding(20));
        }

        @Test
        @DisplayName("Should be capped at the default maximum width of 30")
        void isCappedAtDefaultMaximum() {
            String expected = spaces(DEFAULT_MAX_WIDTH);
            assertEquals(expected, StringUtil.padding(DEFAULT_MAX_WIDTH));
            assertEquals(expected, StringUtil.padding(DEFAULT_MAX_WIDTH + 15)); // Requesting 45 should yield 30
        }
    }

    @Nested
    @DisplayName("padding(int width, int maxPaddingWidth)")
    class PaddingWithCustomMaxWidth {

        @Test
        @DisplayName("Should return correct padding when width is within the custom limit")
        void returnsCorrectPaddingWhenWidthIsWithinMax() {
            assertEquals(spaces(15), StringUtil.padding(15, 30));
        }

        @Test
        @DisplayName("Should be capped at the specified maximum width")
        void isCappedAtCustomMaximum() {
            assertEquals(spaces(5), StringUtil.padding(20, 5));
            assertEquals(spaces(30), StringUtil.padding(45, 30));
        }

        @Test
        @DisplayName("Should return an empty string when max width is zero")
        void returnsEmptyStringWhenMaxWidthIsZero() {
            assertEquals("", StringUtil.padding(0, 0));
            assertEquals("", StringUtil.padding(21, 0)); // Also test with a width that would otherwise be non-empty
        }

        @Test
        @DisplayName("Should allow unlimited padding when max width is negative")
        void allowsUnlimitedPaddingWhenMaxIsNegative() {
            assertEquals(spaces(30), StringUtil.padding(30, -1));
            assertEquals(spaces(45), StringUtil.padding(45, -1));
        }
    }
}