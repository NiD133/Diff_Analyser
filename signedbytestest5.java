package com.google.common.primitives;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.primitives.SignedBytes;
import org.junit.Test;
import org.jspecify.annotations.NullMarked;

/**
 * Tests for {@link SignedBytes#max(byte...)}.
 */
@NullMarked
public class SignedBytesMaxTest {

    private static final byte LEAST = Byte.MIN_VALUE; // -128
    private static final byte GREATEST = Byte.MAX_VALUE; // 127

    @Test
    public void max_withSingleElement_returnsThatElement() {
        // The max of a single-element array should be the element itself.
        assertThat(SignedBytes.max(LEAST)).isEqualTo(LEAST);
        assertThat(SignedBytes.max(GREATEST)).isEqualTo(GREATEST);
    }

    @Test
    public void max_withMultipleElements_returnsGreatestValue() {
        // Arrange: Create an array with a mix of positive, negative, and boundary values.
        byte[] values = {0, LEAST, -1, GREATEST, 1};

        // Act: Find the maximum value in the array.
        byte result = SignedBytes.max(values);

        // Assert: The result should be the greatest possible byte value.
        assertThat(result).isEqualTo(GREATEST);
    }

    @Test
    public void max_withAllNegativeElements_returnsValueClosestToZero() {
        // Arrange: Create an array containing only negative values.
        byte[] values = {-5, -10, LEAST, -1, -100};

        // Act: Find the maximum value in the array.
        byte result = SignedBytes.max(values);

        // Assert: The result should be the "largest" negative number (the one closest to zero).
        assertThat(result).isEqualTo((byte) -1);
    }
}