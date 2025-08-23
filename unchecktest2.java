package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#accept(IOBiConsumer, Object, Object)}.
 */
class UncheckTest {

    private AtomicReference<String> sideEffectRef1;
    private AtomicReference<String> sideEffectRef2;

    @BeforeEach
    void setUp() {
        sideEffectRef1 = new AtomicReference<>();
        sideEffectRef2 = new AtomicReference<>();
    }

    @Test
    @DisplayName("accept(IOBiConsumer) should wrap a thrown IOException in an UncheckedIOException")
    void testAcceptShouldWrapIOException() {
        // Arrange: An IOBiConsumer that is guaranteed to throw an IOException.
        final IOException cause = new IOException("test exception");
        final IOBiConsumer<Object, Object> throwingConsumer = (t, u) -> {
            throw cause;
        };

        // Act & Assert: Call the method and verify that it throws the expected wrapper exception.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.accept(throwingConsumer, "any", "any");
        });

        // Assert: Verify that the original IOException is the cause of the thrown exception.
        assertEquals(cause, thrown.getCause());
    }

    @Test
    @DisplayName("accept(IOBiConsumer) should execute the consumer with the given arguments on success")
    void testAcceptShouldExecuteConsumer() {
        // Arrange: Define the arguments to be passed to the consumer.
        final String arg1 = "Hello";
        final String arg2 = "World";

        // Act: Call Uncheck.accept with a consumer that captures its arguments.
        Uncheck.accept((t, u) -> {
            sideEffectRef1.set(t);
            sideEffectRef2.set(u);
        }, arg1, arg2);

        // Assert: Verify that the consumer was called with the correct arguments.
        assertEquals(arg1, sideEffectRef1.get());
        assertEquals(arg2, sideEffectRef2.get());
    }
}