package org.apache.commons.io.input.buffer;

import org.junit.Test;

/**
 * Tests for the {@link CircularByteBuffer} class.
 */
public class CircularByteBufferTest {

    /**
     * Tests that the read(byte[], int, int) method throws a NullPointerException
     * when the target buffer argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void readShouldThrowNullPointerExceptionForNullTargetBuffer() {
        // Arrange: Create a buffer instance. The offset and length values are arbitrary
        // as the method should fail before they are used.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final int offset = 30;
        final int length = 30;

        // Act & Assert: Call the read method with a null buffer.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        buffer.read(null, offset, length);
    }
}