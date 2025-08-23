package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on boundary and error conditions.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputInt} with an offset that is outside the
     * bounds of the destination buffer correctly throws an {@link ArrayIndexOutOfBoundsException}.
     */
    @Test
    public void outputIntShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: Set up a small buffer and an offset far beyond its length.
        char[] buffer = new char[10];
        int numberToWrite = 123;
        int invalidOffset = 100;

        // Act & Assert: Attempt the operation and verify the expected exception.
        try {
            NumberOutput.outputInt(numberToWrite, buffer, invalidOffset);
            fail("Expected an ArrayIndexOutOfBoundsException because the offset is out of bounds.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // The exception is expected. For robustness, we can also verify that the
            // exception message correctly reports the index that caused the error.
            assertEquals(String.valueOf(invalidOffset), e.getMessage());
        }
    }
}