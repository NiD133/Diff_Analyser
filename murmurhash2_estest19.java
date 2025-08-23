package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that calling hash32 with a null String input correctly
     * throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void hash32ShouldThrowNullPointerExceptionForNullStringInput() {
        // This call is expected to throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        MurmurHash2.hash32((String) null);
    }
}