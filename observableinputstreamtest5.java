package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream} focusing on error handling scenarios.
 */
class ObservableInputStreamTest {

    /**
     * Tests that attempting to read from a stream that always throws an exception
     * correctly propagates the IOException.
     */
    @Test
    void readBufferFromBrokenStreamThrowsIOException() throws IOException {
        // The try-with-resources statement ensures that the stream's close() method is called.
        // The close() method on a BrokenInputStream also throws an IOException, which is why
        // the test method is declared with 'throws IOException'.
        try (final ObservableInputStream ois = new ObservableInputStream(BrokenInputStream.INSTANCE)) {
            // Asserts that the expected exception is thrown when reading from the broken stream.
            assertThrows(IOException.class, () -> ois.read(new byte[1]));
        }
    }
}