package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Tests that calling skip() with a negative value returns 0 and does not
     * advance the stream, as specified by the InputStream.skip() contract.
     */
    @Test
    public void skipWithNegativeArgumentShouldReturnZero() throws IOException {
        // Arrange
        // A minimal IOContext is required by the MergedStream constructor.
        IOContext ioContext = new IOContext(new BufferRecycler(), null, false);

        // The MergedStream is composed of a prefix buffer and an underlying stream.
        byte[] prefixBuffer = new byte[]{'p', 'r', 'e'};
        InputStream underlyingStream = new ByteArrayInputStream(new byte[]{'f', 'i', 'x'});

        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, prefixBuffer, 0, prefixBuffer.length);

        // Act
        // Attempt to skip a negative number of bytes.
        long bytesSkipped = mergedStream.skip(-100L);

        // Assert
        // According to the InputStream contract, skip() should return 0 for a negative argument.
        assertEquals("Skipping a negative number of bytes should return 0.", 0L, bytesSkipped);

        // Further assert that the stream's position has not changed.
        // The next byte read should be the first byte of the prefix buffer.
        assertEquals("Stream position should not change after a negative skip.", 'p', mergedStream.read());
    }
}