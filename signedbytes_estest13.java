package com.google.common.primitives;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
// The original test class name and inheritance are kept for context,
// but in a real-world scenario, they would likely be simplified to:
// public class SignedBytesTest {
public class SignedBytes_ESTestTest13 extends SignedBytes_ESTest_scaffolding {

    /**
     * Verifies that sortDescending throws a NullPointerException when the input array is null.
     */
    @Test(expected = NullPointerException.class)
    public void sortDescending_shouldThrowNullPointerException_whenArrayIsNull() {
        SignedBytes.sortDescending(null);
    }
}