package com.google.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import org.junit.Test;

/**
 * Tests for {@link MultiInputStream}.
 */
public class MultiInputStreamTest {

    @Test(expected = IndexOutOfBoundsException.class)
    public void read_withInvalidBufferParameters_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange: Create a MultiInputStream. The content of the underlying stream is not
        // relevant, so we use a single, empty source.
        InputStream multiInputStream =
                new MultiInputStream(Collections.singletonList(ByteSource.empty()).iterator());

        byte[] buffer = new byte[8];
        int offset = 6;
        int length = 4; // Invalid: offset + length (10) is greater than buffer.length (8)

        // Act & Assert: Attempting to read with parameters that exceed the buffer's
        // bounds should throw an IndexOutOfBoundsException.
        try {
            multiInputStream.read(buffer, offset, length);
        } finally {
            // Ensure the stream is closed, regardless of the test's outcome.
            multiInputStream.close();
        }
    }
}