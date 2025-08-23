package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf returns -1 (not found) when both the source
     * CharSequence and the search CharSequence are null.
     * The class is documented as being null-safe, and this test verifies that behavior.
     */
    @Test
    public void indexOf_withNullSourceAndNullSearch_shouldReturnNotFound() {
        // Arrange
        final CharSequence nullSource = null;
        final CharSequence nullSearch = null;
        final int startIndex = 120; // The starting index should not affect the outcome.
        final int expected = -1;

        // Act
        final int actual = CharSequenceUtils.indexOf(nullSource, nullSearch, startIndex);

        // Assert
        assertEquals(expected, actual);
    }
}