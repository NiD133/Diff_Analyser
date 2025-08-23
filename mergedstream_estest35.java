package com.fasterxml.jackson.core.io;

import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the MergedStream class, focusing on the available() method.
 */
public class MergedStreamTest {

    /**
     * Tests that the available() method correctly delegates to the underlying stream
     * when the internal prepended buffer is null.
     */
    @Test
    public void availableShouldDelegateToUnderlyingStreamWhenBufferIsNull() throws IOException {
        // Arrange
        // 1. Create an underlying input stream with a known number of available bytes.
        byte[] streamData = new byte[]{10, 20, 30, 40, 50};
        InputStream underlyingStream = new ByteArrayInputStream(streamData);

        // 2. Create a MergedStream with a null buffer. This configuration should
        //    force it to delegate calls to the underlying stream.
        //    The IOContext is not used by the available() method, so we can pass null.
        //    The start and end parameters for the buffer are also irrelevant here.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, null, 0, 0);

        // Act
        // Call the available() method on the MergedStream.
        int availableBytes = mergedStream.available();

        // Assert
        // The result should match the number of bytes available from the underlying stream.
        assertEquals("available() should return the count from the underlying stream when the internal buffer is null.",
                streamData.length, availableBytes);
    }
}