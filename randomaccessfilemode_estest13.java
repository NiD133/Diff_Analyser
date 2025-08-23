package org.apache.commons.io;

import org.junit.Test;

/**
 * Tests for {@link RandomAccessFileMode#valueOfMode(String)}.
 */
public class RandomAccessFileMode_ESTestTest13 { // Note: Class name kept for consistency with the original file.

    /**
     * Tests that {@link RandomAccessFileMode#valueOfMode(String)} throws a
     * {@link NullPointerException} when the input string is null.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void valueOfModeShouldThrowNullPointerExceptionForNullInput() {
        // This call is expected to throw a NullPointerException.
        RandomAccessFileMode.valueOfMode(null);
    }
}