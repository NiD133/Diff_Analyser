package com.fasterxml.jackson.core.io;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.util.BufferRecycler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Understandable unit tests for the {@link MergedStream} class.
 * These tests cover the core functionality, edge cases, and resource management aspects of MergedStream.
 */
public class MergedStreamTest {

    /**
     * Creates a MergedStream instance for testing.
     *
     * @param bufferData The byte array to be prepended to the stream. Can be null.
     * @param streamData The byte array for the underlying InputStream.
     * @return A configured MergedStream instance.
     */
    private MergedStream createStream(byte[] bufferData, byte[] streamData) {
        InputStream underlyingStream = new ByteArrayInputStream(streamData);
        if (bufferData == null) {
            return new MergedStream(null, underlyingStream, null, 0, 0);
        }
        return new MergedStream(null, underlyingStream, bufferData, 0, bufferData.length);
    }

    @Test
    public void shouldReadFromBufferFirstThenFromUnderlyingStream() throws IOException {
        // Arrange
        byte[] buffer = {1, 2};
        byte[] streamData = {3, 4};
        MergedStream mergedStream = createStream(buffer, streamData);

        // Act & Assert: Should read from the buffer first
        assertEquals(1, mergedStream.read());
        assertEquals(2, mergedStream.read());

        // Act & Assert: Buffer is now empty, should read from the underlying stream
        assertEquals(3, mergedStream.read());
        assertEquals(4, mergedStream.read());

        // Act & Assert: Both are empty, should return EOF
        assertEquals(-1, mergedStream.read());
    }

    @Test
    public void shouldReadIntoByteArrayFromBufferAndStream() throws IOException {
        // Arrange
        byte[] buffer = {1, 2};
        byte[] streamData = {3, 4, 5, 6};
        MergedStream mergedStream = createStream(buffer, streamData);
        byte[] target = new byte[5];

        // Act: Read 5 bytes. 2 should come from the buffer, 3 from the stream.
        int bytesRead = mergedStream.read(target, 0, target.length);

        // Assert
        assertEquals(5, bytesRead);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, target);
    }

    @Test
    public void shouldReturnCorrectAvailableBytes() throws IOException {
        // Arrange
        byte[] buffer = {1, 2, 3};
        byte[] streamData = {4, 5, 6, 7};
        MergedStream mergedStream = createStream(buffer, streamData);

        // Assert: Initially, available bytes should be the buffer size
        assertEquals(3, mergedStream.available());

        // Act: Read one byte from the buffer
        mergedStream.read();

        // Assert: Available bytes should decrease
        assertEquals(2, mergedStream.available());

        // Act: Read the rest of the buffer
        mergedStream.read(new byte[2]);

        // Assert: After buffer is exhausted, available() should delegate to the underlying stream
        assertEquals(4, mergedStream.available());
    }

    @Test
    public void skip_shouldSkipBytesFromBufferThenStream() throws IOException {
        // Arrange
        byte[] buffer = {1, 2};
        byte[] streamData = {3, 4, 5, 6, 7};
        MergedStream mergedStream = createStream(buffer, streamData);

        // Act: Skip 4 bytes (2 from buffer, 2 from stream)
        long skipped = mergedStream.skip(4);

        // Assert
        assertEquals(4L, skipped);
        // The next byte read should be '5' from the stream
        assertEquals(5, mergedStream.read());
    }

    @Test
    public void skip_withNegativeArgument_shouldReturnZero() throws IOException {
        // Arrange
        // When the buffer is exhausted, skip() delegates to the underlying stream,
        // which should follow the standard InputStream contract of returning 0 for a negative skip.
        MergedStream mergedStream = createStream(null, new byte[]{1, 2});

        // Act
        long skipped = mergedStream.skip(-10);

        // Assert
        assertEquals(0L, skipped);
    }

    @Test
    public void read_withZeroLength_shouldReturnZero() throws IOException {
        // Arrange
        MergedStream mergedStream = createStream(new byte[]{1}, new byte[]{2});

        // Act
        int bytesRead = mergedStream.read(new byte[5], 0, 0);

        // Assert
        assertEquals(0, bytesRead);
    }

    @Test
    public void markSupported_shouldBeFalseWhenBufferIsActive() {
        // Arrange
        MergedStream mergedStream = createStream(new byte[]{1}, new byte[0]);

        // Assert
        // MergedStream does not support mark/reset while the initial buffer is active.
        assertFalse(mergedStream.markSupported());
    }

    @Test
    public void markSupported_shouldDelegateWhenBufferIsExhausted() throws IOException {
        // Arrange: Use a mock stream that supports mark
        InputStream mockInputStream = mock(InputStream.class);
        when(mockInputStream.markSupported()).thenReturn(true);

        MergedStream mergedStream = new MergedStream(null, mockInputStream, new byte[]{1}, 0, 1);

        // Act: Exhaust the buffer
        mergedStream.read();

        // Assert: Now delegates to the underlying stream
        assertTrue(mergedStream.markSupported());
    }

    @Test
    public void close_shouldReleaseBufferAndCloseUnderlyingStream() throws IOException {
        // Arrange
        IOContext context = new IOContext(new BufferRecycler());
        IOContext spyContext = spy(context);
        InputStream mockInputStream = mock(InputStream.class);
        byte[] buffer = new byte[]{1, 2};

        MergedStream mergedStream = new MergedStream(spyContext, mockInputStream, buffer, 0, 2);

        // Act
        mergedStream.close();

        // Assert
        // Verify the buffer was released back to the context
        verify(spyContext).releaseReadIOBuffer(buffer);
        // Verify the underlying stream was closed
        verify(mockInputStream).close();
    }

    @Test(expected = IOException.class)
    public void reset_whenBufferIsExhausted_shouldThrowException() throws IOException {
        // Arrange
        MergedStream mergedStream = createStream(new byte[]{1}, new byte[]{2, 3});

        // Act: Exhaust the buffer
        mergedStream.read();

        // Assert: reset() is not supported after the buffer is gone
        mergedStream.reset(); // Should throw IOException
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withInvalidOffset_shouldThrowException() throws IOException {
        // Arrange
        MergedStream mergedStream = createStream(new byte[]{1}, new byte[0]);
        byte[] target = new byte[5];

        // Act
        mergedStream.read(target, -1, 2); // Invalid offset
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withInvalidLength_shouldThrowException() throws IOException {
        // Arrange
        MergedStream mergedStream = createStream(new byte[]{1}, new byte[0]);
        byte[] target = new byte[5];

        // Act
        mergedStream.read(target, 0, 6); // Invalid length, larger than target array
    }
}