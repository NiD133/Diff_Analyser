package org.apache.commons.io.input;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Unit tests for {@link WindowsLineEndingInputStream}.
 */
public class WindowsLineEndingInputStreamTest {

    /**
     * Tests that calling close() on a stream with an empty underlying input
     * completes without throwing an exception.
     */
    @Test
    public void closeOnEmptyStreamShouldNotThrowException() throws IOException {
        // Arrange: Create a WindowsLineEndingInputStream wrapping an empty stream.
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        final WindowsLineEndingInputStream windowsLineEndingInputStream =
                new WindowsLineEndingInputStream(emptyInputStream, true);

        // Act: Close the stream.
        windowsLineEndingInputStream.close();

        // Assert: The test passes if no exception is thrown.
    }
}