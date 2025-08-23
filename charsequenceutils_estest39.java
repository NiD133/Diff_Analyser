package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Tests for edge cases in {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that regionMatches() returns false when the starting offset for the second
     * CharSequence is negative.
     *
     * <p>The contract for region matching, similar to {@link String#regionMatches},
     * dictates that a negative starting offset is an invalid argument that should
     * cause the comparison to fail immediately.</p>
     *
     * <p>This test also implicitly covers the case where the starting offset for the
     * first CharSequence is out of bounds, which is another reason for returning false.</p>
     */
    @Test
    public void regionMatchesShouldReturnFalseForNegativeSecondOffset() {
        // Arrange
        final CharSequence emptySequence = new StringBuilder();
        final boolean ignoreCase = true;
        final int outOfBoundsStartOffset = 16;
        final int negativeStartOffset = -2908; // The primary invalid parameter under test.
        final int lengthToCompare = 16;

        // Act
        final boolean result = CharSequenceUtils.regionMatches(
            emptySequence,
            ignoreCase,
            outOfBoundsStartOffset,
            emptySequence,
            negativeStartOffset,
            lengthToCompare
        );

        // Assert
        assertFalse("regionMatches should return false for a negative start offset.", result);
    }
}