package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

// The original test class name 'MergedStream_ESTestTest16' suggests it was auto-generated.
// A more descriptive name would be better in a real project.
public class MergedStreamTest {

    /**
     * Tests that a NullPointerException is thrown when MergedStream attempts to release a
     * buffer that was not allocated by its IOContext.
     * <p>
     * The test sets up a MergedStream with a one-byte segment of an externally created buffer.
     * When the single byte is read, the stream attempts to "free" the buffer by returning it
     * to the IOContext's BufferRecycler. This operation is expected to fail with an NPE,
     * as the BufferRecycler was not the source of this buffer.
     */
    @Test(timeout = 4000)
    public void readLastByteFromUnmanagedBuffer_shouldThrowNPEOnRelease() throws Exception {
        // ARRANGE: Set up an IOContext and a buffer that is NOT managed by it.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext context = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.rawReference(null), // Content source is not relevant here.
                true);

        // The underlying stream is not used, as the read will be satisfied by the prefixed buffer.
        InputStream underlyingStream = new ByteArrayInputStream(new byte[0]);

        // This buffer is created externally, not allocated from the context's BufferRecycler.
        byte[] prefixedBuffer = new byte[]{10, 20};
        int startOffset = 1;
        int endOffset = 2; // This creates a one-byte view of the buffer: [20]

        MergedStream mergedStream = new MergedStream(context, underlyingStream, prefixedBuffer, startOffset, endOffset);

        // ACT & ASSERT: Reading the last byte from the buffer triggers its release.
        try {
            mergedStream.read();
            fail("Expected a NullPointerException because the stream tried to release a buffer it does not own.");
        } catch (NullPointerException e) {
            // SUCCESS: The expected exception was caught.
            // The original test verified the exception originated from the IOContext class,
            // confirming the failure occurs during the buffer release process.
            assertNull("The exception message was null in the original test case.", e.getMessage());
        }
    }
}