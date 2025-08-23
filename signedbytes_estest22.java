package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Provides clear and understandable tests for the {@link SignedBytes#max(byte...)} method.
 */
public class SignedBytesMaxTest {

    @Test
    public void max_shouldReturnZero_whenArrayContainsOnlyZeros() {
        // Arrange: Define an input array containing only zero values.
        byte[] numbers = {0, 0, 0};
        byte expectedMax = 0;

        // Act: Call the method under test to find the maximum value.
        byte actualMax = SignedBytes.max(numbers);

        // Assert: Verify that the result is the expected maximum value.
        assertEquals("The maximum value in an array of zeros should be 0.", expectedMax, actualMax);
    }
}