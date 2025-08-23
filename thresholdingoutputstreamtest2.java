package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ThresholdingOutputStream} focusing on scenarios with a broken underlying stream.
 */
class ThresholdingOutputStreamTest {

    /**
     * A simple OutputStream that throws an IOException on all operations.
     * This is a common test utility for simulating I/O failures.
     */
    private static class BrokenOutputStream extends OutputStream {
        static final BrokenOutputStream INSTANCE = new BrokenOutputStream();
        private final IOException exception = new IOException("Broken output stream");

        @Override
        public void write(final int b) throws IOException {
            throw exception;
        }

        @Override
        public void close() throws IOException {
            // Use a distinct message for close to differentiate from write failures.
            throw new IOException("Broken output stream: close()");
        }
    }

    private final int threshold = 1;
    private AtomicInteger thresholdReachedInvocations;

    @BeforeEach
    void setUp() {
        thresholdReachedInvocations = new AtomicInteger(0);
    }

    @Test
    @DisplayName("Write and close should throw exceptions when the underlying stream is broken")
    void whenUnderlyingStreamIsBroken_writeAndCloseThrowExceptions() {
        // This outer assertThrows is specifically to verify that the close() method,
        // called automatically by the try-with-resources statement, throws an IOException.
        final IOException thrownOnClose = assertThrows(IOException.class, () -> {
            // Arrange: Create a stream that is configured to use a BrokenOutputStream.
            // The threshold consumer resets the byte count, but it should never be called.
            try (final ThresholdingOutputStream stream = new ThresholdingOutputStream(threshold,
                tos -> {
                    thresholdReachedInvocations.incrementAndGet();
                    tos.resetByteCount();
                },
                outputStream -> BrokenOutputStream.INSTANCE)) {

                // Assert initial state
                assertFalse(stream.isThresholdExceeded());
                assertEquals(threshold, stream.getThreshold());
                assertEquals(0, stream.getByteCount());
                assertEquals(0, thresholdReachedInvocations.get(), "Threshold consumer should not have been called yet.");

                // Act & Assert: A write attempt should fail due to the BrokenOutputStream.
                assertThrows(IOException.class, () -> stream.write('a'));

                // Assert state after failed write: The stream's state should not change,
                // and the threshold should not be considered reached.
                assertFalse(stream.isThresholdExceeded(), "Threshold should not be exceeded on a failed write.");
                assertEquals(0, stream.getByteCount(), "Byte count should not increase on a failed write.");
                assertEquals(0, thresholdReachedInvocations.get(), "Threshold consumer should not be called on a failed write.");

            } // stream.close() is called here, which is expected to throw the outer exception.
        });

        // Assert: The exception caught was the one from the close() method.
        assertEquals("Broken output stream: close()", thrownOnClose.getMessage());
    }
}