package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on handling of invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@code outputLong} throws a {@link NullPointerException}
     * when the provided byte array buffer is null. This is critical for ensuring
     * the method fails fast with invalid input rather than causing issues later.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionForNullBuffer() {
        // The value and offset are arbitrary, as the null buffer should be checked first.
        long valueToWrite = 0L;
        int offset = 0;
        byte[] nullBuffer = null;

        // This call is expected to throw a NullPointerException.
        NumberOutput.outputLong(valueToWrite, nullBuffer, offset);
    }
}