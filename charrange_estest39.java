package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CharRange}.
 */
public class CharRangeTest {

    /**
     * Tests that the {@code isNotIn()} factory method correctly creates a negated range
     * and automatically swaps the start and end characters if they are provided in
     * reverse order. It also verifies that an iterator can be successfully created
     * for such a range.
     */
    @Test
    public void isNotInFactoryMethodShouldCreateNegatedRangeAndSwapReversedEndpoints() {
        // Arrange: Define start and end characters in a reversed order.
        final char greaterChar = 'Y';
        final char smallerChar = '@';

        // Act: Create the CharRange using the isNotIn factory method.
        // The CharRange constructor is expected to swap the endpoints.
        final CharRange range = CharRange.isNotIn(greaterChar, smallerChar);

        // Assert: Verify the properties of the created range.

        // 1. The range should be marked as negated.
        assertTrue("The range should be negated.", range.isNegated());

        // 2. The start and end characters should be correctly ordered.
        assertEquals("The start character should be the smaller of the two.", smallerChar, range.getStart());
        assertEquals("The end character should be the larger of the two.", greaterChar, range.getEnd());

        // 3. Verify that creating an iterator does not throw an exception.
        // This is a basic check of the iterator's initialization logic for a negated range.
        assertNotNull("The iterator should not be null.", range.iterator());
    }
}