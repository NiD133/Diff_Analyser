package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that a negated CharRange contains another, more restrictive, negated CharRange.
     *
     * A negated range 'A' contains another negated range 'B' if the set of characters
     * excluded by 'B' is a superset of the characters excluded by 'A'.
     */
    @Test
    public void testContainsWithNestedNegatedRanges() {
        // Arrange
        // A range representing all characters EXCEPT those from '+' to '8'.
        CharRange containingRange = CharRange.isNotIn('+', '8');

        // A range representing all characters EXCEPT those from '+' to 'X'.
        // This range excludes more characters, so its set of included characters
        // is a subset of the first range.
        CharRange containedRange = CharRange.isNotIn('+', 'X');

        // Act
        boolean isContained = containingRange.contains(containedRange);

        // Assert
        assertTrue("A negated range should contain another negated range that excludes a larger interval.", isContained);
    }
}