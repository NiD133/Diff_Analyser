package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.indexOf with a negative start index
     * correctly starts the search from the beginning of the CharSequence (index 0).
     * The Javadoc specifies that a negative start index is treated as if it were 0.
     */
    @Test
    public void indexOf_withNegativeStartIndex_shouldStartSearchFromBeginning() {
        // A test string where the character 't' is at a known, non-zero position.
        final CharSequence text = new StringBuilder("012345678tABC");
        final char charToFind = 't';
        final int negativeStartIndex = -1; // Any negative value should be treated as 0.

        // The expected index of 't' when searching from the beginning of the string.
        final int expectedIndex = 9;

        // Call the method under test
        final int actualIndex = CharSequenceUtils.indexOf(text, charToFind, negativeStartIndex);

        // Verify that the method found the character at the correct index
        assertEquals(expectedIndex, actualIndex);
    }
}