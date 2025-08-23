package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that the reOpen() method resets the file pointer to the beginning of the stream.
     * <p>
     * This test first advances the pointer by reading a byte, confirms the pointer has moved,
     * then calls reOpen() and asserts that the pointer is back at position 0.
     */
    @Test
    public void reOpen_whenCalledAfterRead_resetsFilePointerToZero() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a byte array.
        byte[] sourceData = new byte[]{10, 20, 30};
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);

        // Advance the pointer and verify its new position as a precondition.
        fileOrArray.read();
        assertEquals("Precondition failed: Pointer should be at position 1 after reading one byte.", 1L, fileOrArray.getFilePointer());

        // Act: Re-open the stream, which should reset its state.
        fileOrArray.reOpen();

        // Assert: The file pointer should be reset to the beginning.
        long expectedPosition = 0L;
        long actualPosition = fileOrArray.getFilePointer();
        assertEquals("reOpen() should reset the file pointer to 0.", expectedPosition, actualPosition);
    }
}