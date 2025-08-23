package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Tests that saturatedCast correctly returns the same value when the input
     * long is well within the byte's valid range.
     */
    @Test
    public void saturatedCast_withValueInByteRange_returnsSameValue() {
        // Arrange
        long valueToCast = 0L;
        byte expectedByte = 0;

        // Act
        byte actualByte = SignedBytes.saturatedCast(valueToCast);

        // Assert
        assertEquals(expectedByte, actualByte);
    }
}