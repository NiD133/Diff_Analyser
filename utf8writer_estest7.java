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
 * This test suite focuses on the behavior of the UTF8Writer, particularly its exception handling
 * when dealing with buffer overflows and null output streams.
 */
public class UTF8WriterUnderstandabilityTest {

    /**
     * Creates a default IOContext for test purposes.
     * This helper method encapsulates the boilerplate setup required to instantiate UTF8Writer.
     * @return A new IOContext instance with default configurations.
     */
    private IOContext createDefaultIOContext() {
        BufferRecycler bufferRecycler = new BufferRecycler();
        return new IOContext(
                StreamReadConstraints.defaults(),
                StreamWriteConstraints.defaults(),
                ErrorReportConfiguration.defaults(),
                bufferRecycler,
                ContentReference.rawReference("N/A"),
                false // isResourceManaged
        );
    }

    /**
     * Verifies that a NullPointerException is thrown when the writer attempts to flush its
     * internal buffer to a null OutputStream.
     *
     * <p>The test triggers this condition by writing a chunk of data larger than the
     * writer's internal buffer, which forces an automatic flush operation.
     */
    @Test(timeout = 4000)
    public void write_whenBufferOverflowsWithNullStream_throwsNullPointerException() {
        // Arrange
        IOContext ioContext = createDefaultIOContext();
        // Instantiate the writer with a null OutputStream to simulate the failure scenario.
        UTF8Writer utf8Writer = new UTF8Writer(ioContext, (OutputStream) null);

        // The default internal buffer for the writer is 8000 bytes.
        // We create a character array larger than this to guarantee a buffer overflow.
        // All characters are ASCII, so each will be encoded as a single byte.
        char[] oversizedData = new char[8001];
        Arrays.fill(oversizedData, 'a');

        // Act & Assert
        try {
            // This write operation will fill the internal buffer and then attempt to
            // flush it to the underlying stream. Since the stream is null, this
            // flush operation will fail.
            utf8Writer.write(oversizedData);

            // If the write operation completes without an exception, the test fails.
            fail("A NullPointerException was expected but not thrown.");
        } catch (NullPointerException e) {
            // This exception is expected, as the writer tried to access the null OutputStream.
            // Test passes.
        } catch (IOException e) {
            // Catching IOException as well to provide a more informative failure message
            // in case the underlying behavior changes.
            fail("Expected a NullPointerException, but a different IOException was thrown: " + e.getMessage());
        }
    }
}