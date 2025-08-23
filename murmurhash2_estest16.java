package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Unit tests for the {@link MurmurHash2} class.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash32 throws a NullPointerException when the input data array is null.
     */
    @Test(expected = NullPointerException.class)
    public void hash32ShouldThrowNullPointerExceptionForNullInput() {
        // The length parameter is arbitrary, as the method should fail on the null array before using it.
        final int length = 32;
        
        // The cast to (byte[]) is necessary to resolve ambiguity between overloaded hash32 methods.
        MurmurHash2.hash32((byte[]) null, length);
    }
}