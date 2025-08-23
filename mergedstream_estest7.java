package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.io.MergedStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class, focusing on its core
 * functionality of reading from a prefix buffer before the underlying stream.
 */
public class MergedStreamTest {

    /**
     * Verifies that MergedStream first reads all bytes from its internal prefix buffer
     * and then, once the buffer is exhausted, returns -1 (End-Of-File) if the
     * underlying stream is also empty.
     */
    @Test
    public void testRead_whenBufferHasDataAndStreamIsEmpty_readsFromBufferThenReturnsEOF() throws IOException {
        // Arrange
        byte[] prefixBuffer = new byte[] { 42 };
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]); // An empty stream

        // Create a MergedStream with a 1-byte buffer and an empty underlying stream.
        // The IOContext is not used in the read() path, so null is acceptable for this test.
        MergedStream mergedStream = new MergedStream(null, underlyingStream, prefixBuffer, 0, 1);

        // Act
        int byteReadFromBuffer = mergedStream.read();
        int endOfStreamMarker = mergedStream.read();

        // Assert
        assertEquals("Should first read the byte from the prefix buffer.", 42, byteReadFromBuffer);
        assertEquals("Should return -1 (EOF) after the buffer is exhausted.", -1, endOfStreamMarker);
    }
}