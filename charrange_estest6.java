package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharRange}.
 */
public class CharRangeTest {

    @Test
    public void isNotShouldCreateNegatedRangeThatExcludesTheSpecifiedChar() {
        // Arrange: Define the character to be excluded from the range.
        final char excludedChar = 'j';

        // Act: Create a negated range using the factory method.
        // This range represents all characters *except* 'j'.
        final CharRange negatedRange = CharRange.isNot(excludedChar);

        // Assert:
        // The range should not contain the character it was created with.
        assertFalse("A negated range should not contain the excluded character.",
                    negatedRange.contains(excludedChar));

        // The start and end points of the range definition should still be the character itself.
        assertEquals("The start of the range definition should be the excluded character.",
                     excludedChar, negatedRange.getStart());
        assertEquals("The end of the range definition should be the excluded character.",
                     excludedChar, negatedRange.getEnd());
    }
}