package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the skip() method in the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that calling skip() with a negative value does not move the file pointer
     * and returns 0, adhering to the general contract for skip methods in Java I/O.
     */
    @Test
    public void skip_withNegativeValue_shouldReturnZeroAndNotMovePointer() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with some data.
        // The initial file pointer is at position 0.
        byte[] sourceData = new byte[]{10, 20, 30};
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        long negativeBytesToSkip = -2015L;

        // Act: Attempt to skip a negative number of bytes.
        long actualBytesSkipped = fileOrArray.skip(negativeBytesToSkip);

        // Assert: The method should return 0, and the file pointer's position should be unchanged.
        assertEquals("Calling skip() with a negative value should return 0.", 0L, actualBytesSkipped);
        assertEquals("The file pointer should not change after a negative skip.", 0L, fileOrArray.getFilePointer());
    }
}