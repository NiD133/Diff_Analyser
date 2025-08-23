package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void checkedCast_whenValueIsWithinByteRange_shouldReturnSameValue() {
        // Arrange
        long valueToCast = 39L;
        byte expectedValue = 39;

        // Act
        byte actualValue = SignedBytes.checkedCast(valueToCast);

        // Assert
        assertEquals(expectedValue, actualValue);
    }
}