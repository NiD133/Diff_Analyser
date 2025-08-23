package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on exception scenarios.
 */
public class NumberOutputTest {

    /**
     * Verifies that `outputLong()` throws an `ArrayIndexOutOfBoundsException`
     * when the provided character buffer is too small to hold the string
     * representation of the long value.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionForInsufficientBufferSpace() {
        // Arrange
        // A large positive long that requires 19 characters to represent.
        long largeLongValue = 5486124068793688683L;

        // A buffer that is intentionally too small. With an offset of 1,
        // only 2 characters are available (at indices 1 and 2), which is
        // insufficient for the 19-digit number.
        char[] buffer = new char[3];
        int offset = 1;

        // Act & Assert
        // This call is expected to attempt writing past the buffer's boundary,
        // triggering the exception.
        NumberOutput.outputLong(largeLongValue, buffer, offset);
    }
}