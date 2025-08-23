package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link MergedStream} class, focusing on its reading behavior under various conditions.
 */
public class MergedStreamTest {

    /**
     * Tests that when a MergedStream is created with no internal buffer (the "merged" part)
     * and an already-empty underlying input stream, it correctly signals the end of the stream
     * by returning -1 on a read attempt.
     */
    @Test
    public void read_whenBufferIsNullAndStreamIsEmpty_returnsEndOfStream() throws IOException {
        // --- Arrange ---
        // An empty underlying stream.
        InputStream emptyUnderlyingStream = new ByteArrayInputStream(new byte[0]);
        
        // The MergedStream is constructed with a null buffer, meaning it will
        // immediately delegate to the underlying stream. The IOContext is not needed
        // for this scenario, so we can pass null.
        MergedStream mergedStream = new MergedStream(null, emptyUnderlyingStream, null, 0, 0);
        
        byte[] targetBuffer = new byte[256];

        // --- Act ---
        // Attempt to read from the stream.
        int bytesRead = mergedStream.read(targetBuffer, 0, targetBuffer.length);

        // --- Assert ---
        // Since both the internal buffer and the underlying stream are empty,
        // the read operation should return -1 to indicate the end of the stream.
        assertEquals("Should return -1 for an empty stream with no merged buffer", -1, bytesRead);
    }
}