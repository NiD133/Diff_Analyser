package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on its read and pushback functionality.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that reading data after a byte has been pushed back will first consume
     * the pushed-back byte without advancing the underlying file pointer.
     */
    @Test
    public void readFully_afterPushBack_readsPushedBackByteWithoutAdvancingFilePointer() throws IOException {
        // Arrange: Create an accessor with initial data and push a different byte back.
        byte[] sourceData = {(byte) 42}; // The original data in the source.
        RandomAccessFileOrArray accessor = new RandomAccessFileOrArray(sourceData);

        byte pushedBackByte = (byte) 99;
        accessor.pushBack(pushedBackByte);

        // Pre-condition check: Ensure the pointer is at the beginning.
        assertEquals("Pre-condition: File pointer should be at the start.", 0L, accessor.getFilePointer());

        // Act: Read one byte into a buffer.
        byte[] readBuffer = new byte[1];
        accessor.readFully(readBuffer);

        // Assert: The pushed-back byte should be read, and the file pointer should remain unchanged.
        assertEquals("Should have read the pushed-back byte.", pushedBackByte, readBuffer[0]);
        assertEquals("File pointer should not advance when reading a pushed-back byte.", 0L, accessor.getFilePointer());

        // Further verification: A subsequent read should consume from the original source and advance the pointer.
        int nextByteRead = accessor.read();
        assertEquals("Subsequent read should consume from the original source.", sourceData[0], (byte) nextByteRead);
        assertEquals("File pointer should advance after reading from the source.", 1L, accessor.getFilePointer());
    }
}