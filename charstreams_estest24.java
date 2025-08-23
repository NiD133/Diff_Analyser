package com.google.common.io;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.junit.Test;

/**
 * Tests for {@link CharStreams}.
 */
public class CharStreamsTest {

    /**
     * Verifies that attempting to copy characters to a read-only CharBuffer
     * correctly throws a ReadOnlyBufferException.
     */
    @Test(expected = ReadOnlyBufferException.class)
    public void copy_toReadOnlyBuffer_throwsReadOnlyBufferException() throws IOException {
        // Arrange: Create a source buffer with data and a read-only destination buffer.
        CharBuffer source = CharStreams.createBuffer();
        source.put("some data to copy");
        source.flip(); // Prepare the buffer for reading.

        // CharBuffer.wrap(CharSequence) creates a destination buffer that is a read-only view.
        CharBuffer readOnlyDestination = CharBuffer.wrap(source);

        // Act: Attempt to copy from the source to the read-only destination.
        // This is expected to fail because the destination cannot be modified.
        CharStreams.copy(source, readOnlyDestination);

        // Assert: The expected ReadOnlyBufferException is handled by the @Test annotation.
    }
}