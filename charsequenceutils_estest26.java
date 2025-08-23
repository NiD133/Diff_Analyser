package org.apache.commons.lang3;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.CharSequenceUtils}.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that CharSequenceUtils.subSequence throws an IndexOutOfBoundsException
     * when the starting index is negative, which is an invalid argument.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void subSequence_shouldThrowIndexOutOfBoundsException_whenStartIndexIsNegative() {
        // Arrange
        final CharSequence sequence = "any sequence";
        final int negativeStartIndex = -1;

        // Act
        // This call is expected to throw an IndexOutOfBoundsException,
        // which is verified by the @Test(expected=...) annotation.
        CharSequenceUtils.subSequence(sequence, negativeStartIndex);
    }
}