package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that the equals() method returns false when comparing two different ranges,
     * specifically when one is negated and the other is not.
     */
    @Test
    public void testEqualsReturnsFalseForDifferentRangesWithDifferentNegation() {
        // Arrange
        // Create a standard, non-negated range containing a single character 'w'.
        CharRange inclusiveRange = CharRange.isIn('w', 'w');

        // Create a negated range that includes all characters *except* those from '(' to '2'.
        // Note: The constructor sorts start and end, so this is equivalent to isNotIn('(', '2').
        CharRange negatedRange = CharRange.isNotIn('(', '2');

        // Act
        // Compare the two dissimilar ranges for equality.
        boolean areEqual = inclusiveRange.equals(negatedRange);

        // Assert
        // The primary assertion is that the ranges are not equal.
        assertFalse("Ranges should not be equal if one is negated and the other is not.", areEqual);

        // It's also good practice to verify the state of the objects under test.
        // Verify properties of the inclusive range.
        assertEquals("Start of inclusive range", 'w', inclusiveRange.getStart());
        assertEquals("End of inclusive range", 'w', inclusiveRange.getEnd());
        assertFalse("Inclusive range should not be negated", inclusiveRange.isNegated());

        // Verify properties of the negated range.
        assertEquals("Start of negated range", '(', negatedRange.getStart());
        assertEquals("End of negated range", '2', negatedRange.getEnd());
        assertTrue("Negated range should be negated", negatedRange.isNegated());
    }
}