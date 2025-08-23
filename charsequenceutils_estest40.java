package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CharSequenceUtils} class.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#regionMatches(CharSequence, boolean, int, CharSequence, int, int)}
     * returns false when the provided length of the region to compare is negative.
     *
     * <p>This behavior is consistent with {@link String#regionMatches(boolean, int, String, int, int)},
     * which also returns false for a negative length.</p>
     */
    @Test
    public void testRegionMatchesReturnsFalseForNegativeLength() {
        // Arrange
        // Use different CharSequence implementations (String and StringBuilder) to ensure broad compatibility.
        final CharSequence sourceString = "some arbitrary string";
        final CharSequence substring = new StringBuilder("arbitrary");

        final boolean ignoreCase = false;
        final int sourceStartOffset = 5;
        final int substringStartOffset = 0;
        final int negativeLength = -1; // The key condition being tested

        // Act
        final boolean result = CharSequenceUtils.regionMatches(
                sourceString, ignoreCase, sourceStartOffset, substring, substringStartOffset, negativeLength);

        // Assert
        assertFalse("A negative length should cause regionMatches to return false.", result);
    }
}