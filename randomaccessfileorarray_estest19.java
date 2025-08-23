package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray#skipBytes(int)} method.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling skipBytes(0) correctly returns 0 and does not
     * change the internal file pointer's position.
     */
    @Test
    public void skipBytes_whenSkippingZeroBytes_shouldReturnZeroAndNotAdvancePointer() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance with some data.
        // The content of the array is not important for this specific test.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        
        // Sanity check: ensure the initial position is at the beginning.
        assertEquals("Initial file pointer should be at the start", 0L, fileOrArray.getFilePointer());

        // Act: Attempt to skip zero bytes.
        int bytesSkipped = fileOrArray.skipBytes(0);

        // Assert: Verify that the number of skipped bytes is 0 and the file pointer has not moved.
        assertEquals("The number of skipped bytes should be 0", 0, bytesSkipped);
        assertEquals("The file pointer should remain at the start", 0L, fileOrArray.getFilePointer());
    }
}