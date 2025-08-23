package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and exception handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that outputLong() throws an ArrayIndexOutOfBoundsException when the provided
     * character buffer is too small to hold the string representation of the long value.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenBufferIsTooSmall() {
        // GIVEN a long value that requires 9 characters for its string representation.
        long value = 274877907L; // This number has 9 digits.

        // AND a character buffer that is intentionally too small.
        // A buffer of size 8 can only hold indices 0 through 7.
        char[] insufficientBuffer = new char[8];
        int offset = 0;

        // WHEN attempting to write the 9-character long into the 8-character buffer
        // THEN an ArrayIndexOutOfBoundsException is expected.
        NumberOutput.outputLong(value, insufficientBuffer, offset);
    }
}