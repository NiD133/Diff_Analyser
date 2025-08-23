package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Contains tests for the MurmurHash2 class, focusing on handling invalid arguments.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash32() throws an ArrayIndexOutOfBoundsException when called with a negative length.
     * The underlying implementation is expected to fail when it attempts to access an array
     * index calculated from this negative length.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void hash32ShouldThrowExceptionForNegativeLength() {
        // Arrange: Define test inputs
        final byte[] data = new byte[8]; // The array content and size are not critical for this test.
        final int negativeLength = -1;
        final int seed = 0; // The seed value is irrelevant as an exception should be thrown first.

        // Act: Call the method with invalid length, which is expected to throw.
        // Assert: The @Test(expected) annotation handles the exception verification.
        MurmurHash2.hash32(data, negativeLength, seed);
    }
}