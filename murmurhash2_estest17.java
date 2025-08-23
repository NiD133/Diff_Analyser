package org.apache.commons.codec.digest;

import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link MurmurHash2} class, focusing on invalid arguments.
 */
public class MurmurHash2Test {

    /**
     * Tests that {@code hash32(byte[], int)} throws an {@link ArrayIndexOutOfBoundsException}
     * when the specified length is greater than the actual array length.
     */
    @Test
    public void hash32ShouldThrowExceptionWhenLengthIsGreaterThanArraySize() {
        // Arrange: Define an empty byte array and a length that is clearly out of bounds.
        final byte[] data = new byte[0];
        final int lengthGreaterThanDataSize = 32;

        // Act & Assert: Verify that the method call throws the expected exception.
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            MurmurHash2.hash32(data, lengthGreaterThanDataSize);
        });
    }
}