package com.google.common.primitives;

import org.junit.Test;

/**
 * Unit tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Verifies that calling max() on an empty array throws an IllegalArgumentException,
     * as specified by the method's contract.
     */
    @Test(expected = IllegalArgumentException.class)
    public void max_onEmptyArray_throwsIllegalArgumentException() {
        SignedBytes.max(new byte[0]);
    }
}