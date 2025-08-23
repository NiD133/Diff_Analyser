package com.fasterxml.jackson.core.io;

import org.junit.Test;

/**
 * This test class focuses on verifying the behavior of the NumberOutput class,
 * specifically its exception handling for invalid arguments.
 */
public class NumberOutputTest {

    /**
     * Verifies that calling {@link NumberOutput#outputLong(long, byte[], int)} with an offset
     * that is outside the bounds of the provided byte array throws an
     * {@link ArrayIndexOutOfBoundsException}.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void outputLongShouldThrowExceptionWhenOffsetIsOutOfBounds() {
        // Arrange
        // A buffer with a small, fixed size.
        byte[] buffer = new byte[8];
        // An offset that is clearly outside the buffer's bounds.
        int invalidOffset = 100;
        // The actual long value doesn't matter as the bounds check should happen first.
        long value = 12345L;

        // Act
        // Attempt to write the long at an offset beyond the buffer's capacity.
        // This call is expected to throw an ArrayIndexOutOfBoundsException.
        NumberOutput.outputLong(value, buffer, invalidOffset);
    }
}