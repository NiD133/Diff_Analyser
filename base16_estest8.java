package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.Test;

/**
 * Contains tests for the {@link Base16} class, focusing on handling invalid
 * arguments for the decode method.
 */
public class Base16Test {

    /**
     * Tests that the decode method throws an {@link ArrayIndexOutOfBoundsException}
     * when the provided offset is outside the bounds of the input data array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void decodeShouldThrowArrayIndexOutOfBoundsExceptionForInvalidOffset() {
        // Arrange: Create a Base16 codec and a small data array.
        final Base16 base16 = new Base16();
        final byte[] data = new byte[3];
        final Context context = new Context();

        // Define an offset that is clearly out of bounds for the data array.
        final int invalidOffset = 4;
        // The length is irrelevant as the array access at the invalid offset will fail first.
        final int length = 1;

        // Act: Attempt to decode with the invalid offset.
        // The test will pass if this line throws the expected exception.
        base16.decode(data, invalidOffset, length, context);
    }
}