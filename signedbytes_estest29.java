package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void checkedCast_whenValueIsBelowMinByte_throwsIllegalArgumentException() {
        // Arrange: A long value that is just outside the lower bound of the byte range.
        long valueTooSmall = (long) Byte.MIN_VALUE - 1; // -129L

        // Act & Assert: Call checkedCast and verify the correct exception is thrown.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SignedBytes.checkedCast(valueTooSmall)
        );

        // Assert: Verify the exception message is informative.
        assertEquals("Out of range: " + valueTooSmall, thrown.getMessage());
    }
}