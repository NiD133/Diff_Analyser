package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes#max(byte...)}.
 */
public class SignedBytesMaxTest {

    @Test
    public void max_shouldReturnLargestValue_whenArrayContainsPositiveAndZeroValues() {
        // Arrange: Create an array of bytes including a clear maximum value.
        byte[] numbers = {0, 110, 0};
        byte expectedMax = 110;

        // Act: Call the method under test.
        byte actualMax = SignedBytes.max(numbers);

        // Assert: Verify that the result is the expected maximum value.
        assertEquals(expectedMax, actualMax);
    }
}