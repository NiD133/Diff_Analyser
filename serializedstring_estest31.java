package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SerializedString} class, focusing on UTF-8 operations.
 */
public class SerializedStringTest {

    /**
     * Verifies that putUnquotedUTF8() correctly writes the string's byte representation
     * into a ByteBuffer and returns the number of bytes written.
     */
    @Test
    public void putUnquotedUTF8_shouldWriteBytesToBufferAndReturnLength() {
        // Arrange
        String originalString = " ";
        SerializedString serializedString = new SerializedString(originalString);
        
        int bufferCapacity = 100;
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferCapacity);
        
        int expectedBytesWritten = originalString.getBytes(StandardCharsets.UTF_8).length; // Should be 1 for a space

        // Act
        int actualBytesWritten = serializedString.putUnquotedUTF8(byteBuffer);

        // Assert
        // 1. Check if the method returns the correct number of bytes written.
        assertEquals("Should return the number of bytes written.",
                expectedBytesWritten, actualBytesWritten);

        // 2. Check if the buffer's remaining capacity was updated correctly.
        assertEquals("Buffer's remaining capacity should be reduced by bytes written.",
                bufferCapacity - expectedBytesWritten, byteBuffer.remaining());

        // 3. Verify the actual content written to the buffer.
        byteBuffer.flip(); // Prepare the buffer for reading
        byte[] contentWritten = new byte[actualBytesWritten];
        byteBuffer.get(contentWritten);
        
        byte[] expectedContent = originalString.getBytes(StandardCharsets.UTF_8);
        assertArrayEquals("The buffer should contain the correct UTF-8 bytes.",
                expectedContent, contentWritten);
    }
}