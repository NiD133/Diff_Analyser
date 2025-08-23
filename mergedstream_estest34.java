package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

// Note: The original test class name 'MergedStream_ESTestTest34' is preserved for context.
// In a real-world scenario, this would likely be part of a 'MergedStreamTest' class.
public class MergedStream_ESTestTest34 {

    /**
     * Tests that {@link MergedStream#available()} returns 0 when both its sources of data are empty:
     * 1. The internal, prepended buffer.
     * 2. The underlying input stream.
     */
    @Test
    public void available_shouldReturnZero_whenPrependedBufferAndStreamAreEmpty() throws IOException {
        // Arrange
        // 1. An underlying stream that has no available bytes.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        // 2. A buffer to be prepended to the stream.
        byte[] prependedBuffer = new byte[1];

        // 3. A MergedStream where the "view" into the prepended buffer is empty.
        //    This is achieved by setting the start and end pointers to the same position,
        //    simulating a scenario where all prepended data has been read.
        int start = 1;
        int end = 1; // When start == end, the buffer part is considered empty.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, prependedBuffer, start, end);

        // Act
        int availableBytes = mergedStream.available();

        // Assert
        assertEquals("Expected 0 available bytes when both the buffer and stream are empty.", 0, availableBytes);
    }
}