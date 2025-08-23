package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream} when handling error scenarios,
 * particularly with a faulty underlying stream that always throws an IOException.
 *
 * This class focuses on verifying that exceptions from the underlying stream
 * are correctly propagated by the ObservableInputStream.
 */
public class ObservableInputStreamErrorHandlingTest {

    /**
     * Tests that both read and close operations on an ObservableInputStream
     * wrapping a BrokenInputStream correctly propagate the expected IOException.
     *
     * <p>
     * The {@link BrokenInputStream} is a test utility that throws an
     * {@link IOException} on every method call. This test ensures that
     * {@link ObservableInputStream} correctly handles and propagates these
     * exceptions.
     * </p>
     */
    @Test
    void whenInteractingWithBrokenStream_thenIOExceptionsAreThrown() {
        // Arrange: Create an ObservableInputStream with a stream that is designed to fail.
        final ObservableInputStream observableStream = new ObservableInputStream(BrokenInputStream.INSTANCE);

        // Act & Assert: Verify that attempting to read from the stream throws an IOException.
        assertThrows(IOException.class, () -> observableStream.read(new byte[2], 0, 1),
            "Reading from a broken stream should propagate an IOException.");

        // Act & Assert: Verify that closing the stream also throws an IOException.
        // This is crucial as the original test failed due to an unhandled exception on close.
        assertThrows(IOException.class, observableStream::close,
            "Closing a broken stream should propagate an IOException.");
    }
}