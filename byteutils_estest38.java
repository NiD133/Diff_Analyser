package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Contains tests for the {@link ByteUtils} class.
 */
public class ByteUtilsTest {

    /**
     * Tests that {@link ByteUtils#fromLittleEndian(InputStream, int)} correctly decodes a
     * multi-byte zero value and consumes the correct number of bytes from the stream.
     */
    @Test(timeout = 4000)
    public void fromLittleEndianInputStreamShouldCorrectlyDecodeZeroValue() throws IOException {
        // Arrange
        // An input array with more bytes than we plan to read, all initialized to zero.
        final byte[] inputData = new byte[7];
        final InputStream inputStream = new ByteArrayInputStream(inputData);
        final int bytesToRead = 4;
        final int expectedRemainingBytes = inputData.length - bytesToRead; // 7 - 4 = 3

        // Act
        // Read 4 bytes from the stream and interpret them as a little-endian long.
        final long actualValue = ByteUtils.fromLittleEndian(inputStream, bytesToRead);

        // Assert
        // The decoded value should be 0, as the source bytes were all zero.
        assertEquals("The decoded long value should be 0", 0L, actualValue);
        // Verify that exactly 4 bytes were consumed from the stream.
        assertEquals("The number of available bytes should be correct after reading",
                     expectedRemainingBytes, inputStream.available());
    }
}