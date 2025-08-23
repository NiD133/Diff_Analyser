package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on handling invalid inputs.
 */
public class NumberOutputTest {

    /**
     * Verifies that outputInt() throws an ArrayIndexOutOfBoundsException
     * when the provided offset is outside the bounds of the destination buffer.
     * The method is not expected to write anything and should fail fast.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputInt_shouldThrowException_whenOffsetIsOutOfBounds() {
        // Arrange: Define a buffer and an offset that is clearly invalid.
        char[] buffer = new char[5];
        int numberToWrite = -4804;
        int invalidOffset = 10; // An offset well beyond the buffer's capacity (length 5).

        // Act & Assert: Attempt to write the integer at the invalid offset.
        // The @Test(expected) annotation handles the assertion, ensuring the
        // correct exception is thrown.
        NumberOutput.outputInt(numberToWrite, buffer, invalidOffset);
    }
}