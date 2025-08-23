package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on its handling of invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a null character buffer
     * correctly throws a {@link NullPointerException}.
     * <p>
     * The specific numeric value and offset are not relevant for this test,
     * as the null check on the buffer should precede any other logic.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Define inputs where the buffer is null.
        // Any valid long and offset can be used.
        long anyNumber = 12345L;
        int anyOffset = 0;

        // Act & Assert: Call the method with a null buffer,
        // expecting a NullPointerException.
        NumberOutput.outputLong(anyNumber, null, anyOffset);
    }
}