package org.apache.commons.io.input.buffer;

import org.junit.Test;

/**
 * This test suite focuses on the exception-handling behavior of the
 * {@link CircularByteBuffer#add(byte[], int, int)} method.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling {@code add(byte[], int, int)} throws an
     * {@link ArrayIndexOutOfBoundsException} when the requested length
     * to copy exceeds the bounds of the source byte array.
     *
     * This is an important edge case, as the underlying implementation likely
     * uses System.arraycopy, which enforces these bounds.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void add_whenLengthIsGreaterThanSourceArray_shouldThrowArrayIndexOutOfBoundsException() {
        // Arrange: Set up the buffer and the source array.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] sourceArray = new byte[7];
        final int offset = 0;

        // Define a length that is intentionally out of bounds for the source array.
        final int lengthToCopy = 9; // sourceArray.length is 7, so this is invalid.

        // Act: Attempt to add more bytes than are available in the source array.
        // This action is expected to throw the exception.
        buffer.add(sourceArray, offset, lengthToCopy);

        // Assert: The test passes if the expected exception is thrown. This is
        // handled by the @Test(expected=...) annotation.
    }
}