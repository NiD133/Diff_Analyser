package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link SignedBytes#checkedCast(long)}.
 */
public class SignedBytesTest {

    @Test
    public void checkedCast_shouldReturnSameValue_whenValueIsWithinByteRange() {
        // Test the boundaries and a value in the middle
        assertEquals((byte) 127, SignedBytes.checkedCast(Byte.MAX_VALUE));
        assertEquals((byte) -128, SignedBytes.checkedCast(Byte.MIN_VALUE));
        assertEquals((byte) 0, SignedBytes.checkedCast(0L));
        assertEquals((byte) 42, SignedBytes.checkedCast(42L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_shouldThrowException_whenValueIsTooLarge() {
        // Test the first value just outside the upper boundary
        long valueTooLarge = (long) Byte.MAX_VALUE + 1; // 128L
        SignedBytes.checkedCast(valueTooLarge);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkedCast_shouldThrowException_whenValueIsTooSmall() {
        // Test the first value just outside the lower boundary
        long valueTooSmall = (long) Byte.MIN_VALUE - 1; // -129L
        SignedBytes.checkedCast(valueTooSmall);
    }
}