package org.apache.commons.io.input.buffer;

import org.junit.Test;

/**
 * Unit tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that {@link CircularByteBuffer#add(byte[], int, int)} throws a
     * NullPointerException when the provided byte array is null, as per the
     * method's contract.
     */
    @Test(expected = NullPointerException.class)
    public void addWithNullByteArrayShouldThrowNullPointerException() {
        // Arrange: Create a buffer. The size, offset, and length values are
        // irrelevant for this test as the null check should happen first.
        final CircularByteBuffer buffer = new CircularByteBuffer();

        // Act & Assert: Calling add with a null array should throw the exception.
        buffer.add(null, 0, 0);
    }
}