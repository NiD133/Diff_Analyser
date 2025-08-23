package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.test.CustomIOException;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObservableInputStream.Builder}, focusing on the afterRead consumer.
 */
public class ObservableInputStreamBuilderTest {

    /**
     * Tests that the afterRead consumer is successfully invoked after the stream is read.
     */
    @Test
    void testAfterReadConsumerIsCalledWhenStreamIsConsumed() throws IOException {
        // Arrange
        final AtomicBoolean consumerCalled = new AtomicBoolean(false);
        final InputStream observableStream = new ObservableInputStream.Builder()
            .setCharSequence("test data")
            .setAfterRead(bytesRead -> consumerCalled.set(true))
            .get();

        // Act
        try (InputStream stream = observableStream) {
            IOUtils.consume(stream);
        }

        // Assert
        assertTrue(consumerCalled.get(), "The afterRead consumer should have been called.");
    }

    /**
     * Tests that an IOException thrown by the afterRead consumer is propagated to the caller.
     */
    @Test
    void testExceptionInAfterReadConsumerIsPropagated() throws IOException {
        // Arrange
        final String expectedMessage = "Test exception from afterRead consumer";
        final InputStream observableStream = new ObservableInputStream.Builder()
            .setCharSequence("test data")
            .setAfterRead(bytesRead -> {
                throw new CustomIOException(expectedMessage);
            })
            .get();

        // Act & Assert
        try (InputStream stream = observableStream) {
            final CustomIOException thrown = assertThrowsExactly(CustomIOException.class,
                () -> IOUtils.consume(stream),
                "Consuming the stream should propagate the exception from the consumer.");
            assertEquals(expectedMessage, thrown.getMessage(), "The exception message should match the expected message.");
        }
    }
}