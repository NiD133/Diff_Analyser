package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The original test class name and hierarchy are kept for context.
public class CharRange_ESTestTest27 extends CharRange_ESTest_scaffolding {

    /**
     * Tests that CharRange.contains(CharRange) returns false when the outer negated range
     * excludes a character that is present in the inner negated range.
     */
    @Test
    public void contains_shouldReturnFalse_whenOuterNegatedRangeExcludesCharFromInnerNegatedRange() {
        // --- ARRANGE ---

        // Create a range that represents all characters EXCEPT '%'.
        final CharRange rangeExcludingPercent = CharRange.isNot('%');

        // Create a range that represents all characters EXCEPT those from '1' to 'R'.
        // Note: The CharRange constructor automatically orders the start/end characters.
        // The character '%' (ASCII 37) is outside the '1'-'R' range, so it IS included
        // in this negated range.
        final CharRange rangeIncludingPercent = CharRange.isNotIn('R', '1');

        // --- ACT ---

        // Check if the first range (which excludes '%') contains the second range (which includes '%').
        final boolean isContained = rangeExcludingPercent.contains(rangeIncludingPercent);

        // --- ASSERT ---

        // The result must be false. The first range cannot contain the second because
        // '%' is present in rangeIncludingPercent but is explicitly excluded from rangeExcludingPercent.
        assertFalse("A range cannot contain another if it excludes a character that the other includes.", isContained);

        // Also, verify the state of the created ranges to ensure our setup is correct.
        assertTrue("The range excluding '%' should be marked as negated.", rangeExcludingPercent.isNegated());
        assertEquals("The start of the range excluding '%' should be '%'.", '%', rangeExcludingPercent.getStart());

        assertTrue("The range including '%' should be marked as negated.", rangeIncludingPercent.isNegated());
        assertEquals("The start of the range including '%' should be '1' after reordering.", '1', rangeIncludingPercent.getStart());
        assertEquals("The end of the range including '%' should be 'R' after reordering.", 'R', rangeIncludingPercent.getEnd());
    }
}