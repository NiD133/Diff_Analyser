package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Contains tests for the {@link NumberOutput} class, focusing on handling invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputLong} with a null buffer
     * results in a {@code NullPointerException}.
     * <p>
     * The method should perform a null check on its buffer argument before
     * attempting to write to it, thus ensuring robustness against invalid input.
     */
    @Test(expected = NullPointerException.class)
    public void outputLongShouldThrowNullPointerExceptionForNullBuffer() {
        // Arrange: Define inputs for the method under test.
        // The specific long value and offset are irrelevant for this test case,
        // as the null buffer should cause an exception immediately.
        long anyLongValue = 100_000_000_000_000L;
        char[] nullBuffer = null;
        int anyOffset = -152;

        // Act: Call the method that is expected to throw an exception.
        NumberOutput.outputLong(anyLongValue, nullBuffer, anyOffset);

        // Assert: The test framework verifies that a NullPointerException is thrown.
        // This is configured by the `expected` attribute of the @Test annotation.
    }
}