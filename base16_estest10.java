package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.Test;

/**
 * This test suite focuses on verifying the behavior of the Base16 class,
 * particularly its handling of edge cases and invalid inputs.
 */
public class Base16Test {

    /**
     * Tests that the internal encode method throws an ArrayIndexOutOfBoundsException
     * when called with an offset that is outside the bounds of the input data array.
     *
     * This test specifically targets the internal `encode` implementation which does not
     * perform its own bounds checking and relies on the JVM to throw the exception upon
     * invalid memory access.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class, timeout = 4000)
    public void encodeWithOutOfBoundsOffsetShouldThrowException() {
        // Arrange: Set up the encoder, input data, and an out-of-bounds offset.
        final Base16 base16 = new Base16();
        final byte[] data = new byte[5];
        final Context context = new Context();
        
        // An offset greater than or equal to the array length will cause an exception.
        final int outOfBoundsOffset = 10; 
        final int lengthToProcess = 1;

        // Act: Attempt to encode with the invalid offset.
        // The test expects this call to fail with an ArrayIndexOutOfBoundsException.
        base16.encode(data, outOfBoundsOffset, lengthToProcess, context);

        // Assert: The exception is caught and verified by the `expected` parameter
        // in the @Test annotation. No further assertions are needed.
    }
}