package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes#min(byte...)}.
 */
public class SignedBytesMinTest {

    @Test
    public void min_shouldReturnSmallestValue_whenArrayContainsNegativeAndZero() {
        // Arrange
        byte[] numbers = {0, -69};
        byte expectedMin = -69;

        // Act
        byte actualMin = SignedBytes.min(numbers);

        // Assert
        assertEquals(expectedMin, actualMin);
    }
}