package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Contains tests for the {@link ByteUtils} class, focusing on the
 * toLittleEndian(DataOutput, long, int) method.
 */
public class ByteUtilsTest {

    /**
     * Tests that toLittleEndian with a DataOutput correctly writes a number of bytes
     * greater than the standard 8 bytes for a long value. When the value is negative,
     * it should use sign extension for the additional bytes.
     *
     * <p>The original test called this method with a large length (2026) but performed
     * no assertions, only verifying that no exception was thrown. This improved
     * test verifies the actual byte output to ensure correctness.</p>
     */
    @Test
    public void toLittleEndianWithDataOutputShouldWriteMoreThanEightBytesForNegativeValue() throws IOException {
        // Arrange
        final long valueToWrite = -294L; // 0xFFFFFFFFFFFFFEDA in 64-bit two's complement
        final int length = 10;           // A length greater than 8 bytes to test the behavior
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final DataOutput dataOut = new DataOutputStream(baos);

        // The expected little-endian representation of -294L is:
        // DA, FE, FF, FF, FF, FF, FF, FF
        // For a length of 10, sign extension of the negative number should add two more FF bytes.
        final byte[] expectedBytes = new byte[length];
        expectedBytes[0] = (byte) 0xDA;
        expectedBytes[1] = (byte) 0xFE;
        Arrays.fill(expectedBytes, 2, length, (byte) 0xFF);

        // Act
        ByteUtils.toLittleEndian(dataOut, valueToWrite, length);

        // Assert
        final byte[] actualBytes = baos.toByteArray();
        assertEquals("The number of bytes written should match the specified length.", length, actualBytes.length);
        assertArrayEquals("The written bytes should be the correct little-endian representation with sign extension.",
                expectedBytes, actualBytes);
    }
}