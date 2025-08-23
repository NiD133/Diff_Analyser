package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CharRange}.
 * This class focuses on improving the understandability of a specific test case.
 */
public class CharRangeTest {

    /**
     * Tests that a negated single-character range correctly contains a character
     * that is outside the specified negated range.
     */
    @Test
    public void testNegatedSingleCharRangeContainsCharOutsideRange() {
        // Arrange: Create a negated character range that excludes only the character 'N'.
        // This means the range should contain every character *except* 'N'.
        final CharRange negatedRange = CharRange.isNotIn('N', 'N');
        final char characterToTest = '|';

        // Act: Check if the negated range contains a character that is not 'N'.
        final boolean result = negatedRange.contains(characterToTest);

        // Assert: Verify that the character is contained and the range properties are correct.
        assertTrue(
            "A negated range of ['N'] should contain a character like '|' which is not 'N'.",
            result
        );
        assertEquals(
            "The start of the negated range definition should be 'N'.",
            'N',
            negatedRange.getStart()
        );
        assertEquals(
            "The end of the negated range definition should be 'N'.",
            'N',
            negatedRange.getEnd()
        );
    }
}