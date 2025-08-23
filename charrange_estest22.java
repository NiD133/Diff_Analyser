package org.apache.commons.lang3;

import static org.junit.Assert.assertFalse;
import org.junit.Test;

/**
 * Unit tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that {@link CharRange#contains(CharRange)} returns false for two
     * non-overlapping (disjoint) character ranges.
     */
    @Test
    public void containsRange_shouldReturnFalse_forDisjointRanges() {
        // Arrange: Create two distinct, single-character ranges that do not overlap.
        // The factory method `is(char)` is a convenient way to create a range for a single character.
        CharRange rangeW = CharRange.is('w');      // Represents the range ['w']
        CharRange rangeTilde = CharRange.is('~');  // Represents the range ['~']

        // Act & Assert: Verify that one range does not contain the other.
        // The result should be false because the ranges are completely separate.
        assertFalse("A range should not contain another disjoint range.", rangeTilde.contains(rangeW));
    }
}