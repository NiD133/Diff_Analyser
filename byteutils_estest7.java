package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Test suite for the {@link ByteUtils} class.
 * This improved version focuses on clarity and maintainability.
 */
// The class was renamed from the auto-generated "ByteUtils_ESTestTest7"
public class ByteUtilsTest {

    @Test(timeout = 4000)
    public void fromLittleEndianShouldCorrectlyReadLongFromInputStream() throws IOException {
        // Arrange
        // The input byte array is {0, 0, 91, 0, ...}.
        // We will read the first 4 bytes. In little-endian order, this corresponds
        // to the number where the byte at index 2 is the third least significant byte.
        // Value = 0*(2^0) + 0*(2^8) + 91*(2^16) + 0*(2^24) = 5,963,776.
        byte[] sourceBytes = {0, 0, 91, 0, 0, 0, 0};
        InputStream inputStream = new ByteArrayInputStream(sourceBytes);

        final int bytesToRead = 4;
        final int initialStreamSize = sourceBytes.length;

        // The expected value is 0x005B0000 (since 91 is 0x5B in hex).
        // Using a hex literal makes the relationship to the input bytes clearer.
        final long expectedValue = 0x005B0000L;

        // Act
        long actualValue = ByteUtils.fromLittleEndian(inputStream, bytesToRead);

        // Assert
        // 1. Verify that the parsed little-endian value is correct.
        assertEquals("The long value should be correctly parsed from the stream",
                expectedValue, actualValue);

        // 2. Verify that the correct number of bytes were consumed from the stream.
        int expectedRemainingBytes = initialStreamSize - bytesToRead;
        assertEquals("The stream should have the correct number of bytes remaining",
                expectedRemainingBytes, inputStream.available());
    }
}