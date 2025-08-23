package org.apache.commons.codec.binary;

import org.junit.Test;

/**
 * Tests for {@link Base16}.
 */
public class Base16Test {

    /**
     * Tests that the encode method throws an ArrayIndexOutOfBoundsException
     * when the specified offset is greater than the input data's length.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void encodeWithOffsetOutOfBoundsShouldThrowException() {
        // Arrange: Create a Base16 instance and prepare test data.
        final Base16 base16 = new Base16();
        final byte[] data = new byte[10];
        final BaseNCodec.Context context = new BaseNCodec.Context();

        // The offset is intentionally set beyond the bounds of the 'data' array.
        final int invalidOffset = 11;
        final int length = 1; // A valid length to isolate the offset error.

        // Act: Call the encode method with the invalid offset.
        // The @Test(expected=...) annotation will assert that the correct exception is thrown.
        base16.encode(data, invalidOffset, length, context);
    }
}