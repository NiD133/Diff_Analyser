package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class, focusing on the skip() method.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Tests that the skip() method correctly handles a scenario where a byte has been
     * pushed back and the requested skip amount exceeds the total available data.
     *
     * The expected behavior is that skip() will consume the pushed-back byte and all
     * remaining bytes from the underlying source, returning the total number of bytes
     * that were actually skipped. The file pointer should be positioned at the end of
     * the source data.
     */
    @Test
    public void skip_whenSkipAmountExceedsDataWithPushedBackByte_skipsAllAvailableBytes() throws IOException {
        // Arrange: Set up a reader with a 5-byte source and one pushed-back byte.
        final int sourceLength = 5;
        byte[] sourceData = new byte[sourceLength];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // Push one byte back. The total available data is now 6 bytes:
        // 1 pushed-back byte + 5 bytes from the source array.
        reader.pushBack((byte) 22);

        // Define a skip amount that is intentionally larger than the total available data.
        long largeSkipAmount = 114L;

        // Act: Attempt to skip more bytes than are available.
        long actualBytesSkipped = reader.skip(largeSkipAmount);

        // Assert: Verify that only the available bytes were skipped and the pointer is at the end.
        long expectedBytesSkipped = 1 + sourceLength; // 1 for the pushed-back byte + source length
        assertEquals("The number of bytes skipped should be the sum of the pushed-back byte and the source data length.",
                expectedBytesSkipped, actualBytesSkipped);

        long expectedFinalPosition = sourceLength;
        assertEquals("The file pointer should be at the end of the source data after skipping all available bytes.",
                expectedFinalPosition, reader.getFilePointer());
    }
}