package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#run(IORunnable, Supplier)}.
 */
@DisplayName("Uncheck.run(IORunnable, Supplier<String>)")
class UncheckRunWithMessageTest {

    private static final String CAUSE_MESSAGE = "CauseMessage";
    private static final String CUSTOM_MESSAGE = "Custom message";

    @Test
    @DisplayName("should execute the runnable when no I/O error occurs")
    void runShouldExecuteRunnableSuccessfully() {
        // Arrange
        final byte[] data = {'a', 'b'};
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        final IORunnable successfulRunnable = () -> inputStream.skip(1);
        final Supplier<String> messageSupplier = () -> CUSTOM_MESSAGE;

        // Act: This call is expected to succeed without throwing an exception.
        Uncheck.run(successfulRunnable, messageSupplier);

        // Assert: Verify the runnable was executed by checking the stream's state.
        // The first byte 'a' should have been skipped, and the next byte should be 'b'.
        final int nextByte = Uncheck.get(inputStream::read);
        assertEquals('b', nextByte, "The runnable should have been executed, advancing the stream.");
    }

    @Test
    @DisplayName("should wrap and throw UncheckedIOException on I/O error")
    void runShouldThrowUncheckedIOExceptionOnFailure() {
        // Arrange
        final IOException cause = new IOException(CAUSE_MESSAGE);
        final IORunnable failingRunnable = () -> new BrokenInputStream(cause).read();
        final Supplier<String> messageSupplier = () -> CUSTOM_MESSAGE;

        // Act & Assert: Verify that executing the failing runnable throws the expected exception.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.run(failingRunnable, messageSupplier);
        });

        // Assert: Verify the details of the thrown exception.
        assertEquals(CUSTOM_MESSAGE, thrown.getMessage(), "The exception message should match the custom message supplier.");
        assertSame(cause, thrown.getCause(), "The cause of the UncheckedIOException should be the original IOException.");
    }
}