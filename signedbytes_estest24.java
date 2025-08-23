package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void min_shouldReturnZero_whenArrayContainsZeroAndPositiveValues() {
        // Arrange: Create an array with a mix of positive values and zero.
        // Zero is the smallest signed byte in this set.
        byte[] numbers = {16, 5, 127, 0, 8};
        byte expectedMin = 0;

        // Act: Find the minimum value in the array.
        byte actualMin = SignedBytes.min(numbers);

        // Assert: Verify that the minimum value found is indeed zero.
        assertEquals(expectedMin, actualMin);
    }
}