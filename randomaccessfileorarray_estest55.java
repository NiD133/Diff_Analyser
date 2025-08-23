package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Tests that calling the read(byte[]) method with a zero-length buffer
     * correctly returns 0 and does not advance the file pointer. This is the
     * expected behavior according to the Java InputStream contract.
     */
    @Test
    public void read_withEmptyBuffer_returnsZeroAndPointerIsUnchanged() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with some source data
        // and an empty buffer to read into.
        byte[] sourceData = new byte[8];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        byte[] emptyBuffer = new byte[0];

        // Act: Attempt to read data into the empty buffer.
        int bytesRead = fileOrArray.read(emptyBuffer);

        // Assert: Verify that no bytes were read and the file pointer remains at the start.
        assertEquals("Reading into a zero-length buffer should return 0.", 0, bytesRead);
        assertEquals("File pointer should not advance after a zero-length read.", 0L, fileOrArray.getFilePointer());
    }
}