package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer} class, focusing on its interaction
 * with the underlying output stream.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling close() on a UTF8Writer throws a NullPointerException
     * if it was constructed with an underlying stream that is null.
     * <p>
     * The exception is expected to originate from the wrapped stream when UTF8Writer
     * attempts to flush its internal buffer and close the stream.
     */
    @Test(timeout = 4000)
    public void close_whenUnderlyingStreamIsNull_shouldThrowNullPointerException() throws IOException {
        // Arrange: Create a UTF8Writer that wraps a null OutputStream.
        // A BufferedOutputStream is used here to mirror the original test's scenario.
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                new BufferRecycler(),
                ContentReference.unknown(),
                true);

        OutputStream nullOutputStream = null;
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, new BufferedOutputStream(nullOutputStream));

        // Act: Write some data to the writer. This should be buffered internally
        // without causing an immediate error, as the buffer is not yet full.
        utf8Writer.write("some buffered data");

        // Assert: Closing the writer must attempt to flush its buffer to the
        // underlying stream. Since the stream is null, this will cause an NPE.
        try {
            utf8Writer.close();
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // This is the expected behavior. The test passes.
            // The NPE originates from the wrapped stream (BufferedOutputStream)
            // trying to operate on its null delegate stream.
        }
    }
}