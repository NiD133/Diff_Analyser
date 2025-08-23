package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
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
     * Verifies that calling read(byte[], offset, length) with a length of zero
     * returns 0, as specified by the InputStream contract. This should happen
     * without attempting to read from the internal buffer or the underlying stream.
     */
    @Test
    public void readWithZeroLengthShouldReturnZero() throws Exception {
        // Arrange: Set up a MergedStream with a pre-loaded buffer.
        // The IOContext is a required dependency for the constructor.
        IOContext ioContext = createIOContext();

        // The underlying stream is not expected to be used, so an empty one is sufficient.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);
        
        byte[] prependedBuffer = new byte[] { 'a', 'b', 'c' };
        int start = 0;
        int end = prependedBuffer.length;

        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, prependedBuffer, start, end);
        
        byte[] destinationBuffer = new byte[10];

        // Act: Attempt to read zero bytes from the stream.
        int bytesRead = mergedStream.read(destinationBuffer, 0, 0);

        // Assert: The method should return 0, indicating no bytes were read.
        assertEquals("Reading a length of zero should return 0.", 0, bytesRead);
    }

    /**
     * Helper method to create a default IOContext instance for tests.
     */
    private IOContext createIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        ContentReference contentReference = ContentReference.rawReference(false, null);
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                contentReference,
                false);
    }
}