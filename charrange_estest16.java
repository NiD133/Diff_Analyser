package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CharRange}.
 */
public class CharRangeTest {

    @Test
    public void testEqualsReturnsTrueForIdenticalNegatedRanges() {
        // Arrange: Create two identical negated character ranges.
        // A negated range includes all characters EXCEPT the one specified.
        final CharRange rangeA = CharRange.isNot('O');
        final CharRange rangeB = CharRange.isNot('O');

        // Assert: First, confirm the properties of a created range are as expected.
        assertTrue("Range should be negated", rangeA.isNegated());
        assertEquals("Start character should be 'O'", 'O', rangeA.getStart());
        assertEquals("End character should be 'O'", 'O', rangeA.getEnd());

        // Assert: The two identical ranges should be considered equal.
        // This is the primary goal of this test.
        assertEquals("Identical negated ranges should be equal", rangeA, rangeB);
    }
}