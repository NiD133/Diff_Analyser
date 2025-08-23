package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link CharSequenceUtils#regionMatches(CharSequence, boolean, int, CharSequence, int, int)}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void regionMatchesShouldReturnFalseForDifferentRegionsInSameString() {
        // Arrange
        // The test compares two different, non-equal regions within the same CharSequence.
        final CharSequence text = new StringBuilder("', is neither of type Map.Entry nor an Array");

        // Region 1 starts at index 0 with length 16: "', is neither o"
        final int sourceOffset = 0;

        // Region 2 starts at index 7 with length 16: "neither of type "
        final int otherOffset = 7;

        final int length = 16;
        final boolean ignoreCase = true;

        // Act
        // The two regions are not equal, so regionMatches should return false.
        final boolean regionsMatch = CharSequenceUtils.regionMatches(text, ignoreCase, sourceOffset, text, otherOffset, length);

        // Assert
        assertFalse("Expected regionMatches to return false for non-matching regions", regionsMatch);
    }
}