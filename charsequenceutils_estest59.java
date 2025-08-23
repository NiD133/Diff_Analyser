package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the {@link CharSequenceUtils} class, focusing on
 * improving the clarity of an auto-generated test case.
 */
public class CharSequenceUtilsTest {

    /**
     * Tests that {@code CharSequenceUtils.indexOf} for a supplementary code point
     * returns -1 (not found) when the underlying surrogate pair in the CharSequence
     * is split by other characters.
     *
     * <p>A supplementary code point is a Unicode character outside the Basic Multilingual Plane (BMP).
     * In Java's UTF-16 representation, it is encoded as a "surrogate pair" of two
     * {@code char} values. This test ensures that {@code indexOf} correctly identifies
     * that the code point is not present if this pair is not contiguous.</p>
     */
    @Test
    public void indexOfSupplementaryCodePointShouldReturnNotFoundIfSurrogatePairIsSplit() {
        // Arrange
        // U+10000 is a supplementary code point. In decimal, this is 65536.
        // It is represented in a Java String/CharSequence by the surrogate pair: \uD800 and \uDC00.
        final int supplementaryCodePoint = 0x10000;

        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint(supplementaryCodePoint); // builder now contains the two-char surrogate pair "\uD800\uDC00"

        // Split the surrogate pair by inserting other characters in the middle.
        // This makes the sequence an invalid representation of the original code point.
        builder.insert(1, "1973"); // builder now contains "\uD8001973\uDC00"

        final CharSequence sequenceWithSplitSurrogate = builder;
        
        // The Javadoc for indexOf states that a negative start index is treated as 0,
        // so the search should begin from the start of the sequence.
        final int negativeStartIndex = -897;

        // Act
        final int actualIndex = CharSequenceUtils.indexOf(sequenceWithSplitSurrogate, supplementaryCodePoint, negativeStartIndex);

        // Assert
        final int expectedIndexNotFound = -1;
        assertEquals("indexOf should not find the code point because its surrogate pair is split.",
                     expectedIndexNotFound, actualIndex);
    }
}