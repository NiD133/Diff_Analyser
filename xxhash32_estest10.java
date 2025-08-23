package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link XXHash32} class, focusing on error handling for invalid arguments.
 */
public class XXHash32Test {

    /**
     * Verifies that the update() method throws an ArrayIndexOutOfBoundsException
     * when the starting offset is outside the bounds of the input data array.
     * This ensures the method correctly handles invalid offset arguments.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void updateWithOffsetGreaterThanArrayLengthShouldThrowException() {
        // Arrange: Create a hash instance and prepare input data with an invalid offset.
        final XXHash32 hash = new XXHash32();
        final byte[] data = new byte[10];
        final int lengthToRead = 5;
        
        // An offset that is clearly outside the valid range of the 'data' array.
        final int outOfBoundsOffset = 15;

        // Act: Attempt to update the hash with an out-of-bounds offset.
        // The @Test(expected=...) annotation will assert that the expected exception is thrown.
        hash.update(data, outOfBoundsOffset, lengthToRead);
    }
}