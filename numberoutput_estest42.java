package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on boundary conditions and exception handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code NumberOutput.outputInt} with an offset that is
     * outside the bounds of the destination byte array throws an
     * {@link ArrayIndexOutOfBoundsException}. This test ensures that the method
     * correctly validates its input parameters to prevent buffer overflows.
     */
    @Test
    public void outputInt_withOffsetBeyondBufferLength_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Set up the test data.
        byte[] buffer = new byte[16];
        int numberToWrite = 2084322301;
        // Use an offset that is intentionally far beyond the buffer's valid range.
        int invalidOffset = 2084322301;

        // Act & Assert: Execute the method and verify the expected exception.
        try {
            NumberOutput.outputInt(numberToWrite, buffer, invalidOffset);
            fail("Expected an ArrayIndexOutOfBoundsException, but no exception was thrown.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // This is the expected behavior.
            // For a more robust test, we can verify the exception message, which
            // typically includes the invalid index that caused the error.
            assertEquals(String.valueOf(invalidOffset), e.getMessage());
        }
    }
}