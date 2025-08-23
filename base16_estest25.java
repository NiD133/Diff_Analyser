package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec.Context;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Test suite for the {@link Base16} class, focusing on edge cases.
 */
public class Base16Test {

    /**
     * Tests that calling the decode method with a negative length parameter
     * is a no-op and does not modify the input data array. The parent BaseNCodec
     * class is responsible for this behavior, returning immediately when length is negative.
     */
    @Test
    public void decodeWithNegativeLengthShouldNotModifyInput() {
        // Arrange
        final Base16 base16 = new Base16();
        final byte[] data = new byte[10];
        final Context context = new Context();

        // Since a negative length should result in a no-op, the data array is expected to remain unchanged.
        final byte[] expectedData = new byte[10];

        // Act
        // Call decode with a negative length. This should have no effect.
        // The offset value is irrelevant in this case.
        base16.decode(data, 0, -1, context);

        // Assert
        // Verify that the data array was not modified.
        assertArrayEquals(expectedData, data);
    }
}