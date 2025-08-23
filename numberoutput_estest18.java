package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on exception handling
 * for invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@link NumberOutput#outputLong(long, byte[], int)} throws a
     * {@link NullPointerException} when a null byte array is provided as the output buffer.
     * This is the expected behavior, as the method requires a valid buffer to write into.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Define arguments for the method call, using a null buffer.
        long value = 1000L;
        int offset = 1000;
        byte[] nullBuffer = null;

        // Act: Call the method with the null buffer.
        // Assert: The @Test(expected) annotation asserts that a NullPointerException is thrown.
        NumberOutput.outputLong(value, nullBuffer, offset);
    }
}