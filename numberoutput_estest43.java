package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on exception handling for invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputInt} with a null byte array buffer
     * throws a {@link NullPointerException}. The method is expected to perform
     * a null check on the buffer before attempting to write to it.
     */
    @Test(expected = NullPointerException.class)
    public void outputIntWithByteArrayShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // The integer value and offset are arbitrary, as the null buffer
        // should cause an exception before these values are used.
        final int arbitraryValue = 123;
        final int arbitraryOffset = 0;

        NumberOutput.outputInt(arbitraryValue, null, arbitraryOffset);
    }
}