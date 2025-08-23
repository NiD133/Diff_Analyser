package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharRange}.
 */
public class CharRangeTest {

    @Test
    public void isNot_shouldCreateNegatedSingleCharRange() {
        // Arrange
        final char testChar = '}';

        // Act: Create a negated range representing all characters EXCEPT testChar.
        final CharRange negatedRange = CharRange.isNot(testChar);

        // Assert: Verify the range is correctly configured.
        // A negated single-character range still uses the character for its start and end points.
        assertTrue("The range should be marked as negated.", negatedRange.isNegated());
        assertEquals("The start of the range should be the specified character.", testChar, negatedRange.getStart());
        assertEquals("The end of the range should be the specified character.", testChar, negatedRange.getEnd());
    }
}