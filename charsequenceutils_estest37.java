package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Contains tests for the {@link CharSequenceUtils#regionMatches(CharSequence, boolean, int, CharSequence, int, int)} method.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code regionMatches} returns false when the starting offset for the source CharSequence is out of bounds.
     *
     * The original auto-generated test used large, arbitrary numbers for offsets and length,
     * which caused the comparison region to be outside the bounds of the CharSequence.
     * This refactored test clarifies this specific scenario with self-documenting values.
     */
    @Test
    public void testRegionMatchesShouldReturnFalseWhenSourceOffsetIsOutOfBounds() {
        // Arrange
        final CharSequence sourceSequence = "some test data";
        // The 'other' sequence is not relevant to this specific out-of-bounds check,
        // but is required by the method signature.
        final CharSequence otherSequence = "data";

        // Define an offset that is clearly outside the valid range of the source sequence.
        final int outOfBoundsSourceOffset = sourceSequence.length() + 1;
        final int otherOffset = 0;
        final int length = 4;
        final boolean ignoreCase = false;

        // Act
        // Call regionMatches with the out-of-bounds source offset.
        final boolean result = CharSequenceUtils.regionMatches(
            sourceSequence,
            ignoreCase,
            outOfBoundsSourceOffset,
            otherSequence,
            otherOffset,
            length);

        // Assert
        assertFalse("regionMatches should return false for an out-of-bounds source offset.", result);
    }
}