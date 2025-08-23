package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class contains tests for the CharRange class.
 * The following test case was improved for clarity and maintainability.
 */
public class CharRange_ESTestTest14 { // Retaining original class name context

    /**
     * Tests that CharRange.isNotIn() with the same start and end character
     * correctly creates a negated range representing a single character.
     */
    @Test
    public void isNotInWithSameStartAndEndCharShouldCreateNegatedSingleCharRange() {
        // Arrange: Define the character for the range.
        final char testChar = 'N';

        // Act: Create a negated character range for a single character.
        final CharRange range = CharRange.isNotIn(testChar, testChar);

        // Assert: Verify that the range is correctly configured.
        // It should be negated and contain the single specified character.
        assertTrue("The range should be negated.", range.isNegated());
        assertEquals("The start character should be correct.", testChar, range.getStart());
        assertEquals("The end character should be correct.", testChar, range.getEnd());
    }
}