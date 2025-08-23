package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void regionMatches_shouldReturnFalse_whenSubstringOffsetIsOutOfBounds() {
        // The contract for String.regionMatches() specifies that it returns false if the
        // offset into the substring is out of bounds. This test verifies that
        // CharSequenceUtils.regionMatches() upholds a similar contract.
        // Specifically, it checks the case where:
        // substringOffset + length > substring.length()

        // Arrange
        final CharSequence emptySequence = new StringBuffer();
        final int thisStart = 0;
        final int substringStartOffset = 6; // This offset is out of bounds for the empty sequence.
        final int length = 0;
        final boolean ignoreCase = false;

        // Act
        final boolean isMatch = CharSequenceUtils.regionMatches(
                emptySequence,
                ignoreCase,
                thisStart,
                emptySequence,
                substringStartOffset,
                length);

        // Assert
        assertFalse("regionMatches should return false for an out-of-bounds substring offset.", isMatch);
    }
}