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
 * Tests for {@link Uncheck#accept(IOConsumer, Object)}.
 */
class UncheckAcceptTest {

    private AtomicReference<String> sideEffectState;

    @BeforeEach
    void setUp() {
        sideEffectState = new AtomicReference<>();
    }

    @Test
    @DisplayName("Should wrap IOException from the consumer in an UncheckedIOException")
    void acceptShouldThrowUncheckedIOExceptionWhenConsumerThrowsIOException() {
        // Define an IOConsumer that always throws an IOException
        final IOConsumer<String> throwingConsumer = t -> {
            throw new IOException("Test Exception");
        };

        // Assert that calling Uncheck.accept with this consumer results in an UncheckedIOException
        assertThrows(UncheckedIOException.class, () -> Uncheck.accept(throwingConsumer, "anyValue"));
    }

    @Test
    @DisplayName("Should execute the consumer when no exception is thrown")
    void acceptShouldExecuteConsumerSuccessfully() {
        // Define the input value and the consumer that will be executed
        final String inputValue = "expected-value";
        final IOConsumer<String> consumer = sideEffectState::set;

        // Act: Execute the consumer via Uncheck.accept
        Uncheck.accept(consumer, inputValue);

        // Assert: Verify that the consumer was executed and modified the state as expected
        assertEquals(inputValue, sideEffectState.get());
    }
}