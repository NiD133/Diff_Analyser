package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.InputStream;
import java.io.PipedInputStream;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link MergedStream} class, focusing on its behavior
 * regarding stream marking.
 */
public class MergedStreamTest {

    /**
     * Verifies that calling mark() on a MergedStream is safely ignored,
     * as this stream does not support the mark/reset functionality.
     *
     * The test ensures that no exception is thrown even when the stream is
     * constructed with a null internal buffer, confirming that mark() is a no-op.
     */
    @Test
    public void markShouldBeIgnoredWhenMarkIsNotSupported() {
        // Arrange: Set up a MergedStream instance.
        // The IOContext is a required dependency for MergedStream.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                null, // StreamWriteConstraints not relevant for this input stream
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.redacted(),
                true);

        InputStream underlyingStream = new PipedInputStream();

        // Create the MergedStream with a null buffer and invalid start/end indices.
        // This creates an edge-case scenario to ensure mark() does not attempt to
        // interact with the buffer, which would cause a NullPointerException.
        int start = 2541;
        int end = 2;
        MergedStream mergedStream = new MergedStream(ioContext, underlyingStream, null, start, end);

        // Assert: First, confirm the precondition that mark/reset is not supported.
        // This is the fundamental reason the mark() call should be ignored.
        assertFalse("MergedStream should not support the mark() operation.", mergedStream.markSupported());

        // Act: Call the mark() method.
        // The test's success is defined by this action not throwing an exception.
        mergedStream.mark(100); // The read-ahead limit is irrelevant.

        // No further assertions are needed. The absence of an exception proves the behavior.
    }
}