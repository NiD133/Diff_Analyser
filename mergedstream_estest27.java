package com.fasterxml.jackson.core.io;

import org.junit.Test;

import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Contains tests for the {@link MergedStream} class.
 */
public class MergedStreamTest {

    /**
     * Verifies that when the MergedStream's internal buffer is exhausted,
     * it correctly delegates read calls with invalid arguments to the underlying stream.
     * The underlying stream is then expected to throw the appropriate exception.
     */
    @Test(expected = NullPointerException.class)
    public void readWithInvalidArguments_whenBufferIsEmpty_delegatesToUnderlyingStream() throws Exception {
        // Arrange: Create a MergedStream with no internal buffer content.
        // This setup ensures that any read() call is immediately passed to the underlying stream.
        // We use a PipedInputStream as a standard, concrete InputStream implementation.
        InputStream underlyingStream = new PipedInputStream();
        MergedStream mergedStream = new MergedStream(null, underlyingStream, null, 0, 0);

        // Act: Attempt to read from the stream using invalid arguments (null buffer, negative offset/length).
        // This call should be forwarded to the underlying PipedInputStream.
        mergedStream.read(null, -1, -1);

        // Assert: The test expects a NullPointerException to be thrown.
        // The @Test(expected = ...) annotation handles this assertion, failing the test
        // if no exception or a different exception is thrown. This confirms that the
        // invalid call was successfully delegated.
    }
}