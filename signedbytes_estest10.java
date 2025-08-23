package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void compare_whenFirstArgumentIsGreaterThanSecond_returnsPositiveValue() {
        // Arrange
        byte a = 111;
        byte b = 0;

        // Act
        int result = SignedBytes.compare(a, b);

        // Assert
        // The general contract is to return a positive integer.
        assertTrue("Result should be positive when a > b", result > 0);

        // The specific contract for SignedBytes.compare (and Byte.compare)
        // is to return the exact difference.
        assertEquals("Result should be the difference (a - b)", 111, result);
    }
}