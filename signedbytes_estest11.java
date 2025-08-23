package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Tests that checkedCast correctly converts a long value of 0,
     * which is well within the valid range of a byte.
     */
    @Test
    public void checkedCast_withZero_returnsZeroByte() {
        // Arrange
        long valueToCast = 0L;
        byte expectedResult = (byte) 0;

        // Act
        byte actualResult = SignedBytes.checkedCast(valueToCast);

        // Assert
        assertEquals(expectedResult, actualResult);
    }
}