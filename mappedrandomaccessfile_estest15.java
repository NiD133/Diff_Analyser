package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MappedRandomAccessFile#read(byte[], int, int)} method.
 */
public class MappedRandomAccessFileReadTest {

    private static final String TEST_FILE_PATH = "test_read.tmp";

    /**
     * Verifies that calling the read(byte[], int, int) method with a length of zero
     * and a negative offset correctly returns -1 without altering the file state.
     * <p>
     * This test case covers a specific edge case. Unlike standard Java I/O classes that might
     * return 0 for a zero-length read or throw an {@link IndexOutOfBoundsException} for a
     * negative offset, this implementation is expected to return -1.
     * </p>
     */
    @Test
    public void read_withZeroLengthAndNegativeOffset_shouldReturnMinusOne() throws IOException {
        // Arrange: Create a temporary file and a buffer for the read operation.
        File testFile = new File(TEST_FILE_PATH);
        MappedRandomAccessFile mraf = null;
        
        try {
            mraf = new MappedRandomAccessFile(TEST_FILE_PATH, "rw");
            byte[] buffer = new byte[10];
            int negativeOffset = -56;
            int zeroLength = 0;

            // Act: Attempt to read zero bytes with a negative offset.
            int bytesRead = mraf.read(buffer, negativeOffset, zeroLength);

            // Assert: Verify that the method returns -1 and the file pointer has not moved.
            assertEquals("The number of bytes read should be -1 for a zero-length read.", -1, bytesRead);
            assertEquals("The file pointer should not advance.", 0L, mraf.getFilePointer());
            
        } finally {
            // Cleanup: Ensure the file is closed and deleted to avoid resource leaks.
            if (mraf != null) {
                mraf.close();
            }
            testFile.delete();
        }
    }
}