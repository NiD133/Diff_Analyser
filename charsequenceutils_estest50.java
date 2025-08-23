package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Contains improved test cases for {@link CharSequenceUtils} with a focus on understandability.
 */
public class CharSequenceUtilsUnderstandabilityTest {

    /**
     * Tests that {@link CharSequenceUtils#lastIndexOf(CharSequence, CharSequence, int)}
     * correctly searches from the end of the sequence when the specified start index
     * is greater than the sequence's length.
     *
     * <p>This scenario verifies that when searching for a sequence within itself,
     * and providing a large starting index, the method correctly returns 0, which is the
     * index of the last (and only) occurrence.</p>
     */
    @Test
    public void lastIndexOf_shouldSearchFromEnd_whenStartIndexExceedsLength() {
        // Given: A CharSequence and a starting index that is well beyond its length.
        // The original test obscurely used the character '2' (ASCII value 50) as the start index.
        CharSequence text = new StringBuffer("2");
        int startIndex = 50;
        int expectedIndex = 0;

        // When: The last index of the text within itself is searched for, starting from the out-of-bounds index.
        int actualIndex = CharSequenceUtils.lastIndexOf(text, text, startIndex);

        // Then: The method should find the occurrence at index 0, because the search
        // defaults to starting from the end of the CharSequence.
        assertEquals(expectedIndex, actualIndex);
    }
}