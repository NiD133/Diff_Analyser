package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.OutputStream;

/**
 * Contains tests for the {@link UTF8Writer} class.
 * This refactored test focuses on clarity and standard testing practices.
 */
public class UTF8WriterTest {

    /**
     * Verifies that calling {@link UTF8Writer#flush()} throws a
     * {@link NullPointerException} when the writer is constructed with a null
     * {@link OutputStream}. This ensures that the flush operation is correctly
     * delegated to the underlying stream.
     */
    @Test(expected = NullPointerException.class)
    public void flushShouldThrowNPEWhenOutputStreamIsNull() throws Exception {
        // Arrange: Create the necessary context and a UTF8Writer with a null stream.
        // An IOContext is a required dependency for the UTF8Writer.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                null, // ContentReference is not relevant for this test.
                false); // 'isResourceManaged' does not affect this test's outcome.

        OutputStream nullOutputStream = null;
        UTF8Writer writer = new UTF8Writer(ioContext, nullOutputStream);

        // Act: Attempt to flush the writer.
        // This action is expected to throw a NullPointerException because it will try
        // to call flush() on the underlying null OutputStream.
        writer.flush();

        // Assert: The exception is verified by the @Test(expected) annotation.
    }
}