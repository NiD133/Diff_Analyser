package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link CharRange} class.
 */
public class CharRangeTest {

    /**
     * Tests that a negated CharRange is considered to contain itself.
     * For example, the range [^*] (representing all characters except '*')
     * should contain itself.
     */
    @Test
    public void negatedRangeShouldContainItself() {
        // Arrange: Create a negated range that includes all characters except '*'.
        CharRange negatedRange = CharRange.isNot('*');

        // Sanity-check the setup to ensure the range is as expected.
        assertTrue("The range should be negated.", negatedRange.isNegated());
        assertEquals("The start character of the excluded range should be '*'.", '*', negatedRange.getStart());
        assertEquals("The end character of the excluded range should be '*'.", '*', negatedRange.getEnd());

        // Act: Check if the negated range contains itself.
        boolean result = negatedRange.contains(negatedRange);

        // Assert: The result should be true.
        assertTrue("A negated range should always contain itself.", result);
    }
}