package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.Base16;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

/**
 * Test suite for {@link Base16}.
 */
public class Base16Test {

    /**
     * Tests that the decode method throws an ArrayIndexOutOfBoundsException
     * when the provided offset is outside the bounds of the input data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDecodeWithInvalidOffsetThrowsException() {
        // Arrange
        final Base16 base16 = new Base16();
        final byte[] data = new byte[5]; // An array of size 5
        final BaseNCodec.Context context = new BaseNCodec.Context();

        // Act: Attempt to decode from an offset that is outside the array's bounds.
        // The offset (5) is an invalid index for an array of size 5 (valid indices are 0-4).
        final int invalidOffset = 5;
        final int length = 1;
        base16.decode(data, invalidOffset, length, context);

        // Assert: The test expects an ArrayIndexOutOfBoundsException to be thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}