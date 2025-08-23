package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Tests that saturatedCast returns Byte.MIN_VALUE for any long value
     * that is less than the minimum possible byte value.
     */
    @Test
    public void saturatedCast_whenValueIsTooSmall_returnsByteMinValue() {
        // Arrange: A long value well below the minimum byte value of -128.
        long valueBelowMinimum = -2776L;

        // Act: Perform the saturated cast.
        byte result = SignedBytes.saturatedCast(valueBelowMinimum);

        // Assert: The result should be saturated to the minimum byte value.
        assertEquals(Byte.MIN_VALUE, result);
    }
}