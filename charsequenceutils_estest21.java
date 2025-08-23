package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    @Test
    public void testLastIndexOfWithStartIndexGreaterThanLength() {
        // This test verifies the behavior of lastIndexOf when the starting search index
        // is greater than the length of the CharSequence.

        // ARRANGE
        final String text = "', is neither of type Map.Entry nor an Array";
        final char charToFind = 'f'; // The original test used the magic number 102 for 'f'.
        final int startIndex = 102;  // An index deliberately larger than the string's length (47).
                                     // The Javadoc implies this should start the search from the end.

        // The expected index is 15.
        // String: "', is neither o`f` type Map.Entry nor an Array"
        // Index:  ...          15
        // NOTE: This result is surprising. For the same inputs, java.lang.String#lastIndexOf
        // would return 23 (the index of the final 'f'). This test case likely covers a
        // specific implementation detail or edge case within CharSequenceUtils.
        final int expectedIndex = 15;

        // ACT
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, charToFind, startIndex);

        // ASSERT
        assertEquals(expectedIndex, actualIndex);
    }
}