package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains improved tests for {@link CharSequenceUtils}.
 * The original test was auto-generated and difficult to understand.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code CharSequenceUtils.indexOf} returns -1 when the character
     * to search for is a negative value.
     *
     * <p>The original test was confusing because it used a very large, complex
     * CharSequence and seemed to expect an exception. However, the correct behavior
     * of {@code indexOf} is to return -1 for invalid (negative) code points,
     * as they cannot be present in a CharSequence. This test verifies this
     * documented and implemented behavior clearly and concisely.</p>
     */
    @Test
    public void indexOfWithNegativeSearchCharShouldReturnNotFound() {
        // Arrange
        // Use a StringBuilder to test a non-String CharSequence implementation.
        final CharSequence text = new StringBuilder("A test string for indexOf.");
        // A negative value is not a valid Unicode code point.
        final int invalidSearchChar = -1790;
        final int startIndex = 0;

        // Act
        final int result = CharSequenceUtils.indexOf(text, invalidSearchChar, startIndex);

        // Assert
        assertEquals("indexOf should return -1 for a negative search character.", -1, result);
    }
}