package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on error handling with invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that {@link NumberOutput#outputInt(int, char[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is far beyond
     * the bounds of the output buffer.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntShouldThrowExceptionForOffsetBeyondBufferLength() {
        // Arrange: A buffer that is much smaller than the offset we'll use.
        char[] buffer = new char[16];
        int valueToWrite = 1000;
        // An offset deliberately chosen to be extremely large and invalid.
        int invalidOffset = 2084322301;

        // Act & Assert: This call is expected to fail with an ArrayIndexOutOfBoundsException
        // because the offset is far greater than the buffer's capacity.
        NumberOutput.outputInt(valueToWrite, buffer, invalidOffset);
    }
}