package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that the equals() method correctly returns false when comparing two
     * fundamentally different ranges (one inclusive, one negated, with different characters).
     */
    @Test
    public void testEqualsReturnsFalseForDissimilarRanges() {
        // Arrange: Create two distinct CharRange objects.
        
        // An inclusive range containing only the character '+'
        CharRange inclusiveRange = CharRange.isIn('+', '+');
        
        // A negated range containing all characters EXCEPT '7'
        CharRange negatedRange = CharRange.isNotIn('7', '7');

        // Pre-assertion: Verify the initial state of the ranges to ensure our setup is correct.
        // This is not the main assertion of the test.
        assertEquals("Start of inclusive range should be '+'", '+', inclusiveRange.getStart());
        assertEquals("End of inclusive range should be '+'", '+', inclusiveRange.getEnd());
        assertFalse("Inclusive range should not be negated", inclusiveRange.isNegated());

        assertEquals("Start of negated range should be '7'", '7', negatedRange.getStart());
        assertEquals("End of negated range should be '7'", '7', negatedRange.getEnd());
        assertTrue("Negated range should be marked as negated", negatedRange.isNegated());

        // Act & Assert: The CharRange.equals() method checks for start, end, and the negated flag.
        // Since these ranges differ in all three aspects, they must not be equal.
        assertFalse("An inclusive range should not be equal to a different, negated range",
                inclusiveRange.equals(negatedRange));
        
        // For completeness, check for symmetry in the equals comparison.
        assertFalse("A negated range should not be equal to a different, inclusive range",
                negatedRange.equals(inclusiveRange));
    }
}