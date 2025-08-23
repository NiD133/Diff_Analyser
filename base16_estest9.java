package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for the Base16 class, focusing on edge cases for the decode method.
 */
public class Base16Test {

    /**
     * Tests that the decode method handles a negative length parameter gracefully
     * by performing no operation and not modifying the input buffer or context.
     * This is the expected behavior for a "no work to do" scenario.
     */
    @Test
    public void decodeWithNegativeLengthShouldBeANoOp() {
        // Arrange: Set up the codec, a context, and a buffer with known data.
        final Base16 base16 = new Base16();
        final Context context = new Context();
        final byte[] data = new byte[]{10, 20, 30};
        final byte[] originalData = data.clone(); // Create a copy for later comparison.

        // Act: Call the decode method with a negative length.
        // The offset value is arbitrary since no processing should occur.
        base16.decode(data, 0, -1, context);

        // Assert: The data buffer should remain completely unchanged.
        assertArrayEquals("The buffer should not be modified for a negative length.", originalData, data);
    }
}