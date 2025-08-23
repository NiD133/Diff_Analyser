package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link UTF8Writer}.
 * This focuses on improving the understandability of a generated test case.
 */
public class UTF8WriterTest {

    /**
     * Verifies that the constructor throws an {@link IllegalStateException}
     * when attempting to create a second {@link UTF8Writer} with the same
     * {@link IOContext} instance.
     * <p>
     * An {@link IOContext} is designed for a single reader/writer lifecycle,
     * as it manages buffer allocation. Reusing it should be prevented.
     */
    @Test
    public void constructor_whenReusingIOContext_shouldThrowIllegalStateException() {
        // Arrange: Set up an IOContext and an output stream.
        // An IOContext manages buffer recycling and should not be shared across
        // multiple active writer instances.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.REDACTED_CONTENT,
                true);
        OutputStream outputStream = new ByteArrayOutputStream();

        // Create the first writer successfully. This action allocates the internal
        // write buffer from the IOContext.
        new UTF8Writer(ioContext, outputStream);

        // Act & Assert: Attempt to create a second writer with the same IOContext.
        try {
            new UTF8Writer(ioContext, outputStream);
            fail("Expected an IllegalStateException to be thrown for reusing an IOContext.");
        } catch (IllegalStateException e) {
            // The IOContext's allocWriteEncodingBuffer() method should prevent
            // being called a second time, which is the expected behavior.
            assertEquals("Trying to call same allocXxx() method second time", e.getMessage());
        }
    }
}