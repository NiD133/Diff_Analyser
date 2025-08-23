package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Test suite for the XXHash32 class.
 */
public class XXHash32Test {

    /**
     * Tests that the update method throws an ArrayIndexOutOfBoundsException
     * when the provided offset and length are out of the bounds of the input array.
     * This is the standard, expected behavior for such methods in Java.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void updateShouldThrowExceptionForOutOfBoundsParameters() {
        // Arrange: Create a hasher instance and a small data array.
        XXHash32 xxHash32 = new XXHash32();
        byte[] data = new byte[25];

        // Define an offset and length that are clearly out of bounds for the data array.
        // The sum of offset and length would cause an access far beyond the array's end.
        int outOfBoundsOffset = 1_336_530_510;
        int outOfBoundsLength = 1_336_530_510;

        // Act: Attempt to update the hash with parameters that exceed the array's bounds.
        // Assert: The test expects an ArrayIndexOutOfBoundsException to be thrown,
        // which is handled by the @Test(expected=...) annotation.
        xxHash32.update(data, outOfBoundsOffset, outOfBoundsLength);
    }
}