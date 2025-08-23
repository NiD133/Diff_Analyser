package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

// Note: The original test class name and inheritance are kept as per the prompt's context.
// In a real-world scenario, these would be cleaned up (e.g., to `NumberOutputTest`).
public class NumberOutput_ESTestTest50 extends NumberOutput_ESTest_scaffolding {

    /**
     * Verifies that {@link NumberOutput#outputLong(long, byte[], int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the provided offset is outside
     * the bounds of the destination byte array.
     */
    @Test
    public void outputLongShouldThrowExceptionForOutOfBoundsOffset() {
        // Arrange: A buffer of a fixed size and an offset that is clearly out of bounds.
        byte[] buffer = new byte[12];
        int outOfBoundsOffset = 57;
        // The actual long value is not relevant for this boundary check, so any value will do.
        long anyLongValue = 1L;

        // Act & Assert: Call the method and verify that the expected exception is thrown.
        try {
            NumberOutput.outputLong(anyLongValue, buffer, outOfBoundsOffset);
            fail("Expected an ArrayIndexOutOfBoundsException due to an out-of-bounds offset.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Success: The correct exception type was caught.
            // For better validation, we can check if the exception message includes the invalid offset.
            String expectedMessageContent = String.valueOf(outOfBoundsOffset);
            assertTrue(
                "Exception message should contain the out-of-bounds offset.",
                e.getMessage().contains(expectedMessageContent)
            );
        }
    }
}