package com.fasterxml.jackson.core.io;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link NumberOutput} class, focusing on edge cases and exception handling.
 */
public class NumberOutputTest {

    /**
     * Tests that {@code outputInt} throws an {@link ArrayIndexOutOfBoundsException}
     * when the provided offset is outside the bounds of the buffer.
     * The method should fail immediately before attempting to write any digits.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: Create a buffer and an offset that points just outside its bounds.
        // The valid indices for this buffer are 0 through 4.
        char[] buffer = new char[5];
        int outOfBoundsOffset = 5;
        int anyNumber = 123; // The actual number doesn't matter, as the offset check should fail first.

        // Act: Attempt to write the integer at the invalid offset.
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        NumberOutput.outputInt(anyNumber, buffer, outOfBoundsOffset);

        // Assert: The 'expected' parameter of the @Test annotation handles the exception assertion.
        // If the exception is not thrown, the test will fail automatically.
    }

    /**
     * Alternative implementation using a try-catch block for developers who prefer that style
     * or need to inspect the exception.
     */
    @Test
    public void outputIntShouldThrowExceptionWhenOffsetIsOutOfBounds_withTryCatch() {
        // Arrange
        char[] buffer = new char[5];
        int outOfBoundsOffset = 5;
        int anyNumber = 123;

        // Act & Assert
        try {
            NumberOutput.outputInt(anyNumber, buffer, outOfBoundsOffset);
            fail("Expected an ArrayIndexOutOfBoundsException because the offset is outside the buffer's bounds.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Success: The expected exception was thrown.
            // No further assertions are needed for this test case.
        }
    }
}