package org.apache.commons.lang3;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@link CharSequenceUtils#regionMatches(CharSequence, boolean, int, CharSequence, int, int)}
     * correctly returns true when the comparison length is zero.
     */
    @Test
    public void testRegionMatchesWithZeroLengthReturnsTrue() {
        // Arrange
        final CharSequence emptyCharSequence = new StringBuilder();

        // Act
        // A region match with a length of zero should always be true,
        // regardless of the other parameters.
        final boolean result = CharSequenceUtils.regionMatches(
                emptyCharSequence, false, 0, emptyCharSequence, 0, 0);

        // Assert
        assertTrue("A zero-length region should always match", result);
    }
}