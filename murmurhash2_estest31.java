package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash32 method throws an ArrayIndexOutOfBoundsException when the
     * provided length parameter is greater than the actual length of the input byte array.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash32ShouldThrowExceptionWhenLengthExceedsArrayBounds() {
        // Arrange: Create an empty byte array and define a length that is out of bounds.
        final byte[] data = new byte[0];
        final int lengthGreaterThanDataSize = 1;
        final int seed = 12345; // The seed value is arbitrary for this test.

        // Act: Call the method with parameters that should cause an exception.
        // The test will pass only if an ArrayIndexOutOfBoundsException is thrown.
        MurmurHash2.hash32(data, lengthGreaterThanDataSize, seed);
    }
}