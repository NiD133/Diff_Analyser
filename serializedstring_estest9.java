package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on its interaction with ByteBuffers.
 */
public class SerializedStringTest {

    /**
     * Verifies that putQuotedUTF8() correctly handles a ByteBuffer with zero capacity
     * by writing no bytes and returning 0.
     */
    @Test
    public void putQuotedUTF8ShouldReturnZeroWhenBufferHasNoCapacity() {
        // Arrange: Create a SerializedString and a destination buffer with no available space.
        // The content of the string is not critical here, as any attempt to write to a
        // zero-capacity buffer should result in zero bytes written.
        SerializedString serializedString = new SerializedString("any content");
        ByteBuffer zeroCapacityBuffer = ByteBuffer.allocate(0);

        // Act: Attempt to write the quoted UTF-8 representation into the buffer.
        int bytesWritten = serializedString.putQuotedUTF8(zeroCapacityBuffer);

        // Assert: The method should report that zero bytes were written.
        assertEquals("Should write 0 bytes to a buffer with no capacity", 0, bytesWritten);
    }
}