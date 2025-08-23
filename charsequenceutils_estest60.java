package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the CharSequenceUtils class.
 * The original test was automatically generated and has been refactored for clarity.
 */
// The original test class name and inheritance are kept for context.
public class CharSequenceUtils_ESTestTest60 extends CharSequenceUtils_ESTest_scaffolding {

    /**
     * Tests that {@link CharSequenceUtils#indexOf(CharSequence, int, int)} correctly
     * handles a negative start index by treating it as 0.
     *
     * <p>According to the method's Javadoc, a negative start index should have the same
     * effect as a start index of 0, causing the search to begin from the start of the
     * CharSequence. This test also verifies this behavior with a supplementary Unicode
     * code point, which is represented by a surrogate pair in a Java CharSequence.</p>
     */
    @Test
    public void indexOfShouldTreatNegativeStartIndexAsZeroAndFindSupplementaryChar() {
        // Arrange
        // The code point 65571 (U+10023) is a supplementary character that requires
        // a surrogate pair for its representation in a CharSequence.
        final int supplementaryCodePoint = 65571;
        final CharSequence textWithSupplementaryChar = new StringBuilder().appendCodePoint(supplementaryCodePoint);
        
        // A negative start index should be treated as 0.
        final int negativeStartIndex = -2194;
        final int expectedIndex = 0;

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(textWithSupplementaryChar, supplementaryCodePoint, negativeStartIndex);

        // Assert
        assertEquals("The character should be found at index 0 when the start index is negative.",
                expectedIndex, actualIndex);
    }
}