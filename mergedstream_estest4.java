package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that the skip() method correctly advances the stream's position
     * when the requested number of bytes to skip is fully contained within the
     * initial prefix buffer.
     */
    @Test
    public void shouldSkipBytesEntirelyWithinPrefixBuffer() throws Exception {
        // Arrange
        // 1. Define a prefix buffer with known content. This buffer is "pushed back"
        //    and should be read first by the MergedStream.
        byte[] prefixBuffer = new byte[]{10, 20, 30, 40, 50};

        // 2. The underlying stream is not used in this scenario, so an empty one is sufficient.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        // 3. IOContext is a required parameter, so we create a default instance for the test.
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);

        // 4. Create the MergedStream, configured to read from the start of our prefix buffer.
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, prefixBuffer, 0, prefixBuffer.length);

        // Act
        // Skip a number of bytes that is smaller than the total size of the prefix buffer.
        long bytesToSkip = 3L;
        long actualBytesSkipped = mergedStream.skip(bytesToSkip);

        // Assert
        // Verify that the number of skipped bytes returned by the method is correct.
        assertEquals(bytesToSkip, actualBytesSkipped);

        // For more robust verification, check the stream's state. After skipping the first 3 bytes
        // (10, 20, 30), the next byte read from the stream should be the 4th byte (40).
        int nextByte = mergedStream.read();
        assertEquals(40, nextByte);
    }
}