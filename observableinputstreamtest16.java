package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of {@link ObservableInputStream} when reading after the stream has been closed.
 */
@DisplayName("ObservableInputStream: Read after close behavior")
class ObservableInputStreamReadAfterCloseTest {

    /**
     * Creates an ObservableInputStream backed by a ByteArrayInputStream with predefined data.
     *
     * @return a new ObservableInputStream instance.
     */
    private ObservableInputStream createObservableStreamWithData() {
        final byte[] data = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        return new ObservableInputStream(inputStream);
    }

    @Test
    @DisplayName("read() after close() on a stream wrapping ByteArrayInputStream should succeed")
    void readAfterCloseOnByteArrayInputStreamShouldSucceed() throws IOException {
        // Arrange
        // The try-with-resources statement ensures the stream is ultimately closed.
        // We explicitly close it within the block to test the post-close behavior.
        try (InputStream observableStream = createObservableStreamWithData()) {
            // Act
            observableStream.close();

            // Assert
            // The Javadoc for ByteArrayInputStream.close() states that it has no effect.
            // Therefore, a subsequent read() should succeed and return the first byte of data.
            // This test verifies that ObservableInputStream correctly proxies this behavior.
            final int firstByte = observableStream.read();
            assertNotEquals(IOUtils.EOF, firstByte,
                "Reading from a closed stream wrapping a ByteArrayInputStream should not return EOF.");
        }
    }
}