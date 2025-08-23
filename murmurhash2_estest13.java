package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash64() throws a NullPointerException when the input data array is null.
     * The behavior is expected regardless of the provided length parameter.
     */
    @Test(expected = NullPointerException.class)
    public void hash64ShouldThrowNullPointerExceptionForNullData() {
        // The length parameter is arbitrary; the method should fail on the null array first.
        final int irrelevantLength = 10;
        MurmurHash2.hash64(null, irrelevantLength);
    }
}