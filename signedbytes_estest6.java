package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void min_shouldReturnSmallestValueInArray() {
        // Arrange: Create an array of bytes and define the expected minimum value.
        byte[] numbers = {(byte) 39, (byte) 110, (byte) 54};
        byte expectedMin = (byte) 39;

        // Act: Call the method under test to find the minimum value.
        byte actualMin = SignedBytes.min(numbers);

        // Assert: Verify that the actual minimum value matches the expected one.
        assertEquals(expectedMin, actualMin);
    }
}