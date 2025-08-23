package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that closing the {@link BoundedReader} also closes the underlying reader.
     */
    @Test
    void shouldCloseUnderlyingReaderWhenClosed() throws IOException {
        // Arrange: Create a flag to track if the underlying reader has been closed.
        final AtomicBoolean isUnderlyingReaderClosed = new AtomicBoolean(false);

        // Arrange: Create a custom reader that sets the flag when its close() method is called.
        // This allows us to verify that BoundedReader correctly propagates the close() call.
        final Reader underlyingReader = new StringReader("test data") {
            @Override
            public void close() {
                isUnderlyingReaderClosed.set(true);
                // super.close() is not strictly necessary here as StringReader.close() is a no-op,
                // but it's good practice to call it.
            }
        };

        // Act: Instantiate BoundedReader and immediately close it using a try-with-resources block.
        try (Reader boundedReader = new BoundedReader(underlyingReader, 10)) {
            // No operations are needed inside the block.
            // The try-with-resources statement ensures boundedReader.close() is called upon exit.
        }

        // Assert: Verify that the underlying reader was closed as expected.
        assertTrue(isUnderlyingReaderClosed.get(), "Closing BoundedReader should also close the underlying reader.");
    }
}