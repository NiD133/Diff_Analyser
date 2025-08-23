package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that lastIndexOf returns 0 when the search sequence is the same as the
     * source sequence and the search starts at index 0.
     */
    @Test
    public void testLastIndexOf_whenSearchingForSelfFromStartIndexZero_shouldReturnZero() {
        // Arrange
        // The original test created a complex CharSequence "1937true1937true"
        // and searched for itself within itself. We simplify the setup to make the
        // inputs and the purpose of the test clear.
        final CharSequence text = "1937true1937true";
        final CharSequence searchSequence = text; // The search sequence is the text itself.
        final int startIndex = 0;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.lastIndexOf(text, searchSequence, startIndex);

        // Assert
        // The method should find the sequence at the beginning of the text.
        assertEquals(expectedIndex, actualIndex);
    }
}