package com.fasterxml.jackson.core.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link ByteArrayBuilder} class, focusing on edge cases and error handling.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that calling the write() method throws an ArrayIndexOutOfBoundsException
     * if the builder is initialized with a negative internal length pointer. This
     * simulates a corrupted state to ensure the class behaves predictably even
     * under invalid conditions.
     */
    @Test
    public void write_whenInternalLengthIsNegative_shouldThrowArrayIndexOutOfBounds() {
        // Arrange: Create a builder in an invalid state with a negative length.
        // The `fromInitial` factory method is used here to directly manipulate the
        // internal state for this specific test scenario.
        final int negativeInitialLength = -129;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(ByteArrayBuilder.NO_BYTES, negativeInitialLength);

        // Act & Assert: Verify that attempting to write a byte triggers the expected exception.
        // The exception occurs because the internal pointer `_currBlockPtr` is negative.
        ArrayIndexOutOfBoundsException exception = assertThrows(
            ArrayIndexOutOfBoundsException.class,
            () -> builder.write(1) // The value being written is arbitrary.
        );

        // The exception message for ArrayIndexOutOfBoundsException typically contains the invalid index.
        assertEquals(String.valueOf(negativeInitialLength), exception.getMessage());
    }
}