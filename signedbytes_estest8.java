package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link SignedBytes#max(byte...)}.
 */
public class SignedBytesTest {

    @Test
    public void max_shouldReturnTheElement_whenArrayContainsOneElement() {
        // Arrange: Create a byte array with a single element.
        byte[] numbers = {-81};
        byte expectedMax = -81;

        // Act: Find the maximum value in the array.
        byte actualMax = SignedBytes.max(numbers);

        // Assert: The result should be the single element itself.
        assertEquals("The max of a single-element array should be that element",
                expectedMax, actualMax);
    }
}