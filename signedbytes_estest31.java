package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void compare_shouldReturnNegative_whenFirstArgumentIsSmaller() {
        // Arrange
        byte smallerByte = 0;
        byte largerByte = 84;

        // Act
        int result = SignedBytes.compare(smallerByte, largerByte);

        // Assert
        // The general contract is to return a negative value.
        assertTrue("Expected a negative result when comparing a smaller byte to a larger one.", result < 0);

        // The specific implementation behaves like subtraction.
        int expectedResult = smallerByte - largerByte; // 0 - 84 = -84
        assertEquals(expectedResult, result);
    }
}