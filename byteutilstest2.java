package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This test suite evaluates the behavior of the ByteUtils.toLittleEndian method,
 * focusing on edge cases related to its parameters.
 */
public class ByteUtilsTest {

    /**
     * Tests that calling toLittleEndian with a negative length argument
     * results in no bytes being written to the OutputStream.
     * This is because the internal loop condition (i < length) is immediately false.
     */
    @Test
    public void toLittleEndianWithOutputStreamShouldWriteNoBytesForNegativeLength() throws IOException {
        // Arrange: Set up an in-memory output stream and define the input parameters.
        // A negative length is an invalid edge case we want to test.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        long valueToWrite = 8L;
        int negativeLength = -1;

        // Act: Call the method under test with the prepared parameters.
        ByteUtils.toLittleEndian(outputStream, valueToWrite, negativeLength);

        // Assert: Verify that the output stream is empty, confirming that
        // no data was written for a negative length.
        assertEquals("No bytes should be written for a negative length", 0, outputStream.size());
    }
}