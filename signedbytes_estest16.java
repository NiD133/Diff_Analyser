package com.google.common.primitives;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    /**
     * Verifies that join() throws a NullPointerException when the input array is null.
     * The method contract requires a non-null array.
     */
    @Test(expected = NullPointerException.class)
    public void join_withNullArray_throwsNullPointerException() {
        // Act: Call the method under test with a null array argument.
        SignedBytes.join(":", (byte[]) null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}