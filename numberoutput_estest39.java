package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * Unit tests for {@link NumberOutput} focusing on invalid arguments and exception handling.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@code outputInt} with an offset that is outside the
     * bounds of the destination array throws an {@link ArrayIndexOutOfBoundsException}.
     * <p>
     * For performance, the {@code NumberOutput} class does not perform its own
     * bounds checking. Instead, it relies on the underlying JVM array access to
     * enforce boundaries. This test confirms that this expected behavior occurs.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputIntShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange: A small buffer and an offset that is clearly outside its valid range.
        byte[] buffer = new byte[5];
        int outOfBoundsOffset = 739;
        int anyIntValue = 123; // The integer value itself is not relevant to this test.

        // Act & Assert: Attempting to write to the invalid offset should throw.
        // The @Test(expected=...) annotation handles the assertion.
        NumberOutput.outputInt(anyIntValue, buffer, outOfBoundsOffset);
    }
}