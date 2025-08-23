package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class.
 * This is a refactored version of an auto-generated test.
 */
public class MergedStream_ESTestTest3 { // Note: Original class name is kept for consistency.

    /**
     * Verifies that calling skip(0) on a MergedStream correctly returns 0,
     * adhering to the general contract of InputStream.skip().
     * This test specifically covers the case where the stream's internal buffer is empty.
     */
    @Test
    public void skipZeroBytesShouldReturnZero() throws IOException {
        // Arrange: Create a MergedStream with an empty internal buffer.
        // The underlying stream has content, but it should not be accessed when skipping zero bytes.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[]{10, 20, 30});
        byte[] emptyBuffer = new byte[8];
        int bufferStart = 0;
        int bufferEnd = 0; // An empty buffer segment is defined by start == end.

        // The IOContext is not used by the skip() method, so null is acceptable for this specific test.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, emptyBuffer, bufferStart, bufferEnd);

        // Act: Attempt to skip zero bytes from the stream.
        long bytesSkipped = mergedStream.skip(0);

        // Assert: The number of bytes skipped should be 0.
        assertEquals(0L, bytesSkipped);
    }
}