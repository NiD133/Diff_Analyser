package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for the {@link org.apache.commons.lang3.CharRange} class.
 */
public class CharRangeTest {

    @Test
    public void contains_shouldReturnFalseForNonContainedRange() {
        // Arrange
        // Create a range that contains only the character 'w'.
        CharRange rangeW = CharRange.isIn('w', 'w');
        // Create a second, non-overlapping range containing only '~'.
        CharRange rangeTilde = CharRange.is('~');

        // Act & Assert
        // Verify that the first range does not contain the second, non-overlapping range.
        assertFalse(rangeW.contains(rangeTilde));
    }
}