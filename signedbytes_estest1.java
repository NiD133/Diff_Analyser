package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void saturatedCast_withMinValue_shouldReturnMinValue() {
        // Arrange: The input value is already the minimum possible byte value.
        long inputValue = Byte.MIN_VALUE;
        byte expectedValue = Byte.MIN_VALUE;

        // Act: Perform the saturated cast.
        byte actualValue = SignedBytes.saturatedCast(inputValue);

        // Assert: The result should be the same as the input, as it's within the byte range.
        assertEquals(expectedValue, actualValue);
    }
}