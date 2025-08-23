package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

// The original test class name and inheritance are kept to match the provided context.
public class RandomAccessFileOrArray_ESTestTest20 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests the behavior of read() when a byte has been pushed back and the destination
     * for the read operation is the same byte array that backs the RandomAccessFileOrArray.
     * This setup tests an edge case where the initial write (from the pushed-back byte)
     * modifies the source for the subsequent block read within the same operation.
     */
    @Test
    public void readAfterPushBack_whenReadingIntoSourceBuffer_reflectsIntermediateModification() throws IOException {
        // Arrange
        final byte PUSHED_BACK_BYTE = (byte) -123;
        // This buffer serves as BOTH the data source for the reader and the destination for the read operation.
        byte[] buffer = new byte[]{0, 0, 0, 0, 0};
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(buffer);

        reader.pushBack(PUSHED_BACK_BYTE);

        // Act
        int bytesRead = reader.read(buffer);

        // Assert
        assertEquals("Should have read the full buffer length", 5, bytesRead);

        // The expected result is due to a feedback effect:
        // 1. The first byte read is the pushed-back byte (-123). It is written to buffer[0].
        //    The buffer's state becomes: [-123, 0, 0, 0, 0].
        // 2. The reader then performs a block read for the remaining 4 bytes from the underlying
        //    source (which is the now-modified buffer), starting from position 0.
        // 3. The first byte of this block read is buffer[0], which is now -123. This value is
        //    written to the destination at buffer[1].
        // 4. The rest of the block read proceeds, reading from source positions 1, 2, and 3
        //    (which are still 0) and writing to buffer[2], buffer[3], and buffer[4].
        byte[] expectedBufferState = new byte[]{PUSHED_BACK_BYTE, PUSHED_BACK_BYTE, 0, 0, 0};
        assertArrayEquals(
                "The buffer should reflect the modification from the pushed-back byte affecting the subsequent read",
                expectedBufferState,
                buffer
        );
    }
}