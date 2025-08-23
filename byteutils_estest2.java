package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ByteUtils}.
 */
public class ByteUtilsTest {

    /**
     * Tests that calling toLittleEndian with a negative length does not write any bytes
     * to the output stream and does not throw an exception. This verifies an edge case
     * where the internal write loop condition is immediately false.
     */
    @Test
    public void toLittleEndianShouldWriteNothingForNegativeLength() throws IOException {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long valueToWrite = 8L;
        int negativeLength = -1;

        // Act
        ByteUtils.toLittleEndian(outputStream, valueToWrite, negativeLength);

        // Assert
        // The method should not write any bytes for a negative length.
        assertEquals("OutputStream should be empty for a negative write length", 0, outputStream.size());
    }
}