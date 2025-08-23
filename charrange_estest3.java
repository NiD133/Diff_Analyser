package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CharRange} class, focusing on the contains(CharRange) method.
 */
public class CharRangeTest {

    /**
     * Tests that {@code contains(CharRange)} returns true when the provided range
     * is fully enclosed within the other range.
     */
    @Test
    public void contains_shouldReturnTrue_whenRangeIsFullyContained() {
        // Arrange: Create a range that is fully contained within another.
        // The range ['P', 'P'] is a sub-range of [';', 'z'].
        final CharRange containingRange = CharRange.isIn(';', 'z');
        final CharRange containedRange = CharRange.is('P');

        // Act: Check if the larger range contains the smaller one.
        final boolean isContained = containingRange.contains(containedRange);

        // Assert: The result should be true.
        assertTrue("Expected the range [;, z] to contain the range [P, P]", isContained);
    }
}