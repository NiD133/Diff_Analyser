package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link SerializedString} class.
 */
public class SerializedStringTest {

    /**
     * Tests that {@link SerializedString#putQuotedUTF8(ByteBuffer)} correctly writes a simple
     * ASCII string, which requires no special JSON quoting, to a ByteBuffer.
     *
     * This test verifies three key outcomes:
     * 1. The method returns the correct number of bytes written.
     * 2. The ByteBuffer's position is advanced by the number of bytes written.
     * 3. The actual bytes written to the buffer match the expected UTF-8 representation.
     */
    @Test
    public void putQuotedUTF8_withSimpleAsciiString_writesCorrectBytesAndUpdatesBuffer() {
        // Arrange: Set up the input string and the target buffer.
        final String inputValue = "8+19F:23Vy=-x";
        final byte[] expectedBytes = inputValue.getBytes(StandardCharsets.UTF_8);
        final int expectedByteCount = expectedBytes.length;

        SerializedString serializedString = new SerializedString(inputValue);
        // Use a buffer with ample capacity to hold the result.
        ByteBuffer targetBuffer = ByteBuffer.allocate(100);

        // Act: Call the method under test.
        int bytesWritten = serializedString.putQuotedUTF8(targetBuffer);

        // Assert: Verify the results.
        // 1. Check that the returned byte count is correct.
        assertEquals("The method should return the number of bytes written.",
                expectedByteCount, bytesWritten);

        // 2. Check that the buffer's position was updated correctly.
        assertEquals("The buffer's position should advance by the number of bytes written.",
                expectedByteCount, targetBuffer.position());

        // 3. Verify the actual content written to the buffer for correctness.
        targetBuffer.flip(); // Prepare the buffer for reading its content.
        byte[] actualBytes = new byte[bytesWritten];
        targetBuffer.get(actualBytes);

        assertArrayEquals("The bytes written to the buffer should match the input string.",
                expectedBytes, actualBytes);
    }
}