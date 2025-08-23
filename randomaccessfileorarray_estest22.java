package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling skipBytes() when the pointer is already at the end of the data source
     * returns -1 and does not change the pointer's position.
     *
     * The return value of -1 signifies that the end-of-file was reached before any bytes could be skipped,
     * which is a specific behavior of this class.
     */
    @Test
    public void skipBytes_whenAtEndOfFile_returnsMinusOneAndDoesNotAdvancePointer() throws IOException {
        // Arrange: Create a data source and explicitly move the pointer to its end.
        byte[] sourceData = new byte[]{ 10, 20 };
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        
        // Using seek() is more direct and clearer than the original test's use of readLine()
        // to advance the pointer to the end of the data.
        fileOrArray.seek(fileOrArray.length());
        long positionAtEnd = fileOrArray.getFilePointer();
        
        // Act: Attempt to skip bytes when already at the end.
        int bytesSkipped = fileOrArray.skipBytes(500);

        // Assert: Check for the specific return value and ensure the pointer has not moved.
        assertEquals("skipBytes should return -1 to indicate the pointer was already at the end of the source.", -1, bytesSkipped);
        assertEquals("The file pointer should remain at the end of the source.", positionAtEnd, fileOrArray.getFilePointer());
    }
}