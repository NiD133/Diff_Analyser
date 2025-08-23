package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Unit tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that the hash64 method throws a NullPointerException when the input
     * byte array is null.
     */
    @Test(expected = NullPointerException.class)
    public void hash64ShouldThrowNullPointerExceptionForNullData() {
        // Define irrelevant parameters for length and seed, as the method should
        // fail on the null data array before these are used.
        final int length = 10;
        final int seed = 123;

        // This call is expected to throw a NullPointerException.
        MurmurHash2.hash64(null, length, seed);
    }
}