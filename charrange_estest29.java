package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that a negated CharRange does not contain another range if their boundaries overlap.
     *
     * <p>This test verifies that a negated range, such as [^'@'-'Y'], correctly reports
     * that it does not contain an inner range, ['8'-'A'], because the inner range
     * includes characters ('@', 'A') that are explicitly excluded by the negated range.</p>
     */
    @Test
    public void testContainsReturnsFalseWhenNegatedRangeOverlapsWithInnerRange() {
        // Arrange
        // A negated range that includes all characters EXCEPT those from '@' to 'Y'.
        // The factory method correctly handles the inverted start/end characters ('Y', '@').
        CharRange negatedRange = CharRange.isNotIn('Y', '@');

        // A standard range that includes all characters from '8' to 'A'.
        CharRange innerRange = CharRange.isIn('8', 'A');

        // Act
        // Check if the negated range contains the inner range.
        boolean isContained = negatedRange.contains(innerRange);

        // Assert
        // The inner range ['8'-'A'] contains characters '@' and 'A'. The negatedRange
        // [^'@'-'Y'] explicitly excludes these. Therefore, the result must be false.
        assertFalse("A negated range should not contain a range it partially excludes", isContained);

        // Also, verify the state of the ranges to ensure the setup is correct.
        assertTrue("The containing range should be negated", negatedRange.isNegated());
        assertEquals("Start of negated range should be '@'", '@', negatedRange.getStart());
        assertEquals("End of negated range should be 'Y'", 'Y', negatedRange.getEnd());
        assertEquals("Start of inner range should be '8'", '8', innerRange.getStart());
        assertEquals("End of inner range should be 'A'", 'A', innerRange.getEnd());
    }
}