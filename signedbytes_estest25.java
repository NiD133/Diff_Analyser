package com.google.common.primitives;

import org.junit.Test;

/**
 * Unit tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test(expected = IllegalArgumentException.class)
    public void min_onEmptyArray_throwsIllegalArgumentException() {
        // The min() method is specified to throw an exception for an empty array,
        // as there is no minimum value to return.
        SignedBytes.min(new byte[0]);
    }
}