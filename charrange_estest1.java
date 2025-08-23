package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link CharRange} class, focusing on equality.
 */
public class CharRangeTest {

    @Test
    public void testEqualsReturnsFalseForDifferentRanges() {
        // Arrange
        // Create a range representing a single character.
        final CharRange rangeA = CharRange.isIn('?', '?'); // Represents the range ['?']

        // Create a different range. Note that the CharRange constructor automatically
        // orders the start and end characters, so isIn('D', '?') becomes the range ['?'...'D'].
        final CharRange rangeB = CharRange.isIn('D', '?'); // Represents the range ['?'...'D']

        // Act & Assert
        // Verify that two ranges with different endpoints are not considered equal.
        // Using two assertions checks for the symmetry of the equals method.
        assertNotEquals(rangeA, rangeB);
        assertNotEquals(rangeB, rangeA);
    }
}