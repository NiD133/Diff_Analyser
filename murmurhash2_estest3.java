package org.apache.commons.codec.digest;

import org.junit.Test;

/**
 * Tests for the {@link MurmurHash2} class, focusing on edge cases and invalid inputs.
 */
public class MurmurHash2Test {

    /**
     * Tests that hash32(String, int, int) throws a NullPointerException
     * when the input string is null.
     */
    @Test(expected = NullPointerException.class)
    public void hash32StringWithRangeShouldThrowNullPointerExceptionForNullInput() {
        // The specific values for 'from' and 'length' are irrelevant here,
        // as the method should throw a NullPointerException before using them.
        MurmurHash2.hash32((String) null, 0, 0);
    }
}