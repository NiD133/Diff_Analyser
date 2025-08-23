package org.apache.commons.io.input.buffer;

import org.junit.Test;
import org.apache.commons.io.input.buffer.CircularByteBuffer;

/**
 * Tests for {@link CircularByteBuffer}.
 */
public class CircularByteBufferTest {

    /**
     * Tests that calling peek() with a null buffer argument throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void peekShouldThrowNullPointerExceptionWhenBufferIsNull() {
        // Arrange: Create a circular buffer instance.
        final CircularByteBuffer buffer = new CircularByteBuffer();
        final byte[] nullTargetBuffer = null;
        
        // The offset and length values are irrelevant for this test, as the null check
        // on the buffer should happen first. We use 0 for simplicity.
        final int irrelevantOffset = 0;
        final int irrelevantLength = 0;

        // Act & Assert: Call peek() with a null buffer, which is expected to throw.
        // The @Test(expected) annotation handles the assertion.
        buffer.peek(nullTargetBuffer, irrelevantOffset, irrelevantLength);
    }
}