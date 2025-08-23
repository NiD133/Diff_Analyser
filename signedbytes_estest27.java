package com.google.common.primitives;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Tests that {@link SignedBytes#saturatedCast(long)} correctly returns
     * {@link Byte#MAX_VALUE} when the input long is greater than any value
     * a byte can hold.
     */
    @Test
    public void saturatedCast_withValueGreaterThanMaxByte_returnsMaxValue() {
        // Arrange: A long value that is larger than Byte.MAX_VALUE.
        long valueTooLarge = 209L;

        // Act: Perform the saturated cast.
        byte result = SignedBytes.saturatedCast(valueTooLarge);

        // Assert: The result should be saturated to the maximum byte value.
        assertEquals(Byte.MAX_VALUE, result);
    }
}