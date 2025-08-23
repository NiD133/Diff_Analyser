package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that regionMatches() returns false when the length parameter is negative.
     * A negative length is an invalid argument for a region comparison,
     * and the method should handle this gracefully by returning false,
     * similar to the behavior of String#regionMatches.
     */
    @Test
    public void regionMatchesShouldReturnFalseForNegativeLength() {
        // Arrange
        final CharSequence cs = "any string";
        final CharSequence substring = "any other string";
        final int negativeLength = -1;
        final int anyValidIndex = 0;
        final boolean ignoreCase = true;

        // Act
        final boolean result = CharSequenceUtils.regionMatches(cs, ignoreCase, anyValidIndex, substring, anyValidIndex, negativeLength);

        // Assert
        assertFalse("A negative length should cause regionMatches to return false.", result);
    }
}