package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test suite for the {@link UTF8Writer} class.
 *
 * Note: The original test class name "UTF8Writer_ESTestTest44" was non-descriptive.
 * This class demonstrates a clearer, more maintainable testing style.
 */
public class UTF8WriterTest { // Renamed for clarity

    /**
     * Creates a default IOContext for testing purposes, encapsulating boilerplate setup.
     * @return A new IOContext instance with default configurations.
     */
    private IOContext createDefaultIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        return new IOContext(
            null, // StreamReadConstraints: not relevant for a writer test.
            StreamWriteConstraints.defaults(),
            ErrorReportConfiguration.defaults(),
            bufferRecycler,
            null, // ContentReference: not relevant for this test.
            false // recycling: not relevant for this test.
        );
    }

    @Test(expected = NullPointerException.class)
    public void write_whenBufferIsNull_shouldThrowNullPointerException() throws IOException {
        // Arrange
        IOContext ioContext = createDefaultIOContext();
        // Use a simple ByteArrayOutputStream as a sink for the writer.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, outputStream);

        char[] nullBuffer = null;
        int offset = -1; // An invalid offset
        int length = 1;  // A valid length

        // Act & Assert
        // This call is expected to throw a NullPointerException because the buffer is null.
        // The exception should be thrown before the invalid offset is checked.
        // The 'expected' attribute on the @Test annotation handles the assertion.
        utf8Writer.write(nullBuffer, offset, length);
    }
}