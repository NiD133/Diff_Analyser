package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#accept(IOTriConsumer, Object, Object, Object)}.
 */
class UncheckAcceptTriConsumerTest {

    private AtomicReference<String> ref1;
    private AtomicReference<String> ref2;
    private AtomicReference<String> ref3;

    @BeforeEach
    void setUp() {
        ref1 = new AtomicReference<>();
        ref2 = new AtomicReference<>();
        ref3 = new AtomicReference<>();
    }

    @Test
    @DisplayName("Should invoke the IOTriConsumer with the correct arguments when no exception is thrown")
    void shouldInvokeConsumerWithCorrectArguments() {
        // Arrange
        final String arg1 = "value1";
        final String arg2 = "value2";
        final String arg3 = "value3";

        // Act: Call Uncheck.accept with a consumer that captures its arguments.
        Uncheck.accept((t, u, v) -> {
            ref1.set(t);
            ref2.set(u);
            ref3.set(v);
        }, arg1, arg2, arg3);

        // Assert: Verify that the consumer was called with the expected arguments.
        assertEquals(arg1, ref1.get());
        assertEquals(arg2, ref2.get());
        assertEquals(arg3, ref3.get());
    }

    @Test
    @DisplayName("Should wrap a thrown IOException in an UncheckedIOException")
    void shouldWrapIOExceptionInUncheckedIOException() {
        // Arrange: A consumer that always throws a specific IOException.
        final String exceptionMessage = "Test I/O failure";
        final IOTriConsumer<Object, Object, Object> throwingConsumer = (t, u, v) -> {
            throw new IOException(exceptionMessage);
        };

        // Act & Assert: Verify that UncheckedIOException is thrown.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.accept(throwingConsumer, "any", "any", "any");
        });

        // Assert: Verify the cause of the thrown exception is the original IOException.
        final Throwable cause = thrown.getCause();
        assertNotNull(cause, "The UncheckedIOException should have a cause.");
        assertInstanceOf(IOException.class, cause);
        assertEquals(exceptionMessage, cause.getMessage());
    }
}