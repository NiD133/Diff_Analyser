package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the {@link CharRange} class, focusing on the equals() method.
 */
public class CharRangeTest {

    /**
     * Tests that two CharRange instances are not considered equal if one is negated
     * and the other is not, even if they are defined by the same character.
     */
    @Test
    public void equals_shouldReturnFalse_whenRangesHaveSameCharButDifferentNegation() {
        // Arrange
        final char ch = '=';
        // A range representing ONLY the character '='
        final CharRange positiveRange = CharRange.is(ch);
        // A range representing all characters EXCEPT '='
        final CharRange negatedRange = CharRange.isNot(ch);

        // Sanity-check that both ranges are based on the same start/end character.
        // This clarifies that the only significant difference is the negation.
        assertEquals(ch, positiveRange.getStart());
        assertEquals(ch, positiveRange.getEnd());
        assertEquals(ch, negatedRange.getStart());
        assertEquals(ch, negatedRange.getEnd());

        // Act & Assert
        // The CharRange.equals() method must check the 'negated' state.
        // Therefore, these two ranges must not be equal.
        // Using assertNotEquals is more semantic than assertFalse(range1.equals(range2)).
        assertNotEquals(positiveRange, negatedRange);
        
        // Also test for symmetry to ensure the equals contract is upheld.
        assertNotEquals(negatedRange, positiveRange);
    }
}