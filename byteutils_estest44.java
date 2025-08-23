package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

// The class name is preserved from the original test suite structure.
public class ByteUtils_ESTestTest44 extends ByteUtils_ESTest_scaffolding {

    /**
     * Tests that toLittleEndian correctly writes a negative long value to a ByteConsumer,
     * sign-extending it to a specified length greater than the standard 8 bytes for a long.
     */
    @Test
    public void toLittleEndianWithByteConsumerShouldWriteNegativeLongWithSignExtension() throws IOException {
        // Arrange
        final long valueToWrite = -2038L; // Hex: 0xFFFFFFFFFFFFF80A
        final int outputLength = 20;

        // The expected little-endian representation of -2038L is:
        // 0x0A, 0xF8, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF
        // Since the requested length (20) is greater than 8, the value is sign-extended.
        // For a negative number, this means padding with 0xFF bytes.
        final byte[] expectedBytes = new byte[outputLength];
        expectedBytes[0] = (byte) 0x0A;
        expectedBytes[1] = (byte) 0xF8;
        // Fill the rest of the array with 0xFF for the long's value and sign extension
        Arrays.fill(expectedBytes, 2, outputLength, (byte) 0xFF);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteUtils.ByteConsumer byteConsumer = new ByteUtils.OutputStreamByteConsumer(outputStream);

        // Act
        ByteUtils.toLittleEndian(byteConsumer, valueToWrite, outputLength);

        // Assert
        final byte[] actualBytes = outputStream.toByteArray();

        assertEquals("The output should have the specified length.", outputLength, actualBytes.length);
        assertArrayEquals("The written bytes should match the little-endian, sign-extended value.",
                expectedBytes, actualBytes);
    }
}