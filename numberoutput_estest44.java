package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and error handling.
 */
// The class name is improved to follow standard Java conventions.
// Original: NumberOutput_ESTestTest44
public class NumberOutputTest {

    /**
     * Verifies that `outputInt` throws an `ArrayIndexOutOfBoundsException`
     * when the provided character buffer is too small to hold the integer's
     * string representation.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputInt_shouldThrowException_whenBufferIsTooSmall() {
        // Arrange
        // The number 1,000,000,000 is 10 digits long and requires a buffer of at least size 10.
        final int numberToWrite = 1_000_000_000;
        
        // We provide a buffer that is intentionally too small to hold the number.
        final char[] smallBuffer = new char[9];
        final int offset = 0;

        // Act
        // This attempt to write the 10-digit number into the 9-char buffer should fail.
        NumberOutput.outputInt(numberToWrite, smallBuffer, offset);

        // Assert
        // The test will pass if an ArrayIndexOutOfBoundsException is thrown,
        // as specified by the @Test(expected=...) annotation.
    }
}