package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream} focusing on its behavior after being closed.
 */
class ObservableInputStreamClosedBehaviorTest {

    private static final Path TEST_FILE = Paths.get("src/test/resources/org/apache/commons/io/abitmorethan16k.txt");

    /**
     * Verifies that an {@link ObservableInputStream} correctly proxies the closed state of its
     * underlying stream. Attempting to read from the stream after it has been closed should
     * result in an {@link IOException}.
     */
    @Test
    void readFromClosedStreamShouldThrowIOException() throws IOException {
        // Arrange: Create an ObservableInputStream wrapping a file stream.
        // The try-with-resources ensures the underlying file stream is always closed,
        // even if the test fails.
        try (ObservableInputStream observableStream = new ObservableInputStream(Files.newInputStream(TEST_FILE))) {
            // Act: Explicitly close the stream to test its post-close behavior.
            observableStream.close();

            // Assert: Verify that a subsequent read operation throws an IOException.
            assertThrows(IOException.class, observableStream::read,
                "Should not be able to read from a closed stream.");
        }
    }
}