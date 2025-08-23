package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link CharSequenceUtils}.
 * This class focuses on the indexOf method.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that indexOf correctly handles a negative start index when searching for an
     * empty CharSequence.
     *
     * <p>The behavior should align with {@link String#indexOf(String, int)}, where a negative
     * start index is treated as 0. Searching for an empty string ("") should return
     * the start index, which in this case becomes 0.</p>
     */
    @Test
    public void indexOf_withEmptySearchSequenceAndNegativeStartIndex_shouldReturnZero() {
        // Arrange
        final CharSequence text = new StringBuffer();
        final CharSequence searchSequence = new StringBuilder();
        final int negativeStartIndex = -1;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(text, searchSequence, negativeStartIndex);

        // Assert
        assertEquals("Searching for an empty sequence with a negative start index should result in 0.",
                expectedIndex, actualIndex);
    }
}