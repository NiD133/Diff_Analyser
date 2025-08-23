package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on handling of invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a null buffer
     * correctly throws a {@code NullPointerException}. The method must not
     * proceed if the destination buffer does not exist.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // Given a valid number and offset, but a null buffer
        long valueToWrite = 99L;
        byte[] nullBuffer = null;
        int offset = 91;

        // When attempting to write the long to the null buffer
        // Then a NullPointerException should be thrown
        NumberOutput.outputLong(valueToWrite, nullBuffer, offset);
    }
}