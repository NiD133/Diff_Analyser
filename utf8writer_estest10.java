package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.ErrorReportConfiguration;
import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.core.util.BufferRecycler;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link UTF8Writer} class.
 */
public class UTF8WriterTest {

    /**
     * Verifies that writing to a UTF8Writer configured with a null OutputStream
     * throws a NullPointerException when the internal buffer needs to be flushed.
     */
    @Test
    public void write_whenBufferFlushesToNullOutputStream_throwsNullPointerException() {
        // Arrange: Set up a standard IOContext, which is required by the UTF8Writer constructor.
        BufferRecycler bufferRecycler = new BufferRecycler();
        IOContext ioContext = new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.REDACTED_CONTENT,
                false);

        // Arrange: Instantiate the writer with a null output stream.
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, (OutputStream) null);

        // Arrange: Create a character array larger than the writer's internal buffer (default is 2000 bytes)
        // to guarantee that a flush operation is triggered.
        char[] largeData = new char[3000];
        Arrays.fill(largeData, 'A');

        // Act & Assert
        try {
            // This write operation will fill the internal buffer and attempt to flush it.
            utf8Writer.write(largeData);
            fail("Expected a NullPointerException because the underlying OutputStream is null.");
        } catch (NullPointerException expected) {
            // This is the expected behavior. The exception is thrown when the writer
            // attempts to write the buffered content to the null output stream.
        } catch (IOException e) {
            fail("An unexpected IOException was thrown: " + e.getMessage());
        }
    }
}