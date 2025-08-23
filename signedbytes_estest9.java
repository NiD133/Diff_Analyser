package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link SignedBytes} class.
 */
public class SignedBytesTest {

    @Test
    public void compare_shouldReturnZero_whenBytesAreEqual() {
        // Arrange: Define two equal byte values.
        byte value1 = 0;
        byte value2 = 0;

        // Act: Compare the two bytes using the method under test.
        int result = SignedBytes.compare(value1, value2);

        // Assert: The result should be 0, indicating the values are equal.
        assertEquals(0, result);
    }
}