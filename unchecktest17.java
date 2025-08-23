package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

import org.apache.commons.io.input.BrokenInputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#getAsLong(IOLongSupplier, Supplier)}.
 */
@DisplayName("Uncheck.getAsLong with a custom message supplier")
class UncheckGetAsLongWithMessageTest {

    private static final String CAUSE_MESSAGE = "CauseMessage";
    private static final String CUSTOM_MESSAGE = "Custom message";

    /**
     * Asserts that the thrown UncheckedIOException has the expected custom message and cause.
     *
     * @param expectedCause The original IOException that was expected to be wrapped.
     * @param thrownException The UncheckedIOException that was actually thrown.
     */
    private void assertUncheckedIOException(final IOException expectedCause, final UncheckedIOException thrownException) {
        assertAll("Thrown Exception Validation",
            () -> assertEquals(CUSTOM_MESSAGE, thrownException.getMessage(),
                "The exception message should match the custom message provided by the supplier."),
            () -> {
                final IOException actualCause = thrownException.getCause();
                assertEquals(expectedCause.getClass(), actualCause.getClass(), "The cause should be the original IOException.");
                assertEquals(CAUSE_MESSAGE, actualCause.getMessage(), "The cause's message should be preserved.");
            }
        );
    }

    @Test
    @DisplayName("should return the long value when the IOLongSupplier succeeds")
    void shouldReturnLongWhenSupplierSucceeds() {
        // Arrange
        final AtomicLong atomicLong = new AtomicLong(0);
        final long expectedValue = 1L;
        final IOLongSupplier successfulSupplier = () -> {
            atomicLong.set(expectedValue);
            return expectedValue;
        };

        // Act
        final long result = Uncheck.getAsLong(successfulSupplier, () -> CUSTOM_MESSAGE);

        // Assert
        assertAll("Successful execution",
            () -> assertEquals(expectedValue, result, "Should return the value from the supplier."),
            () -> assertEquals(expectedValue, atomicLong.get(), "The supplier's side-effect should be observable.")
        );
    }

    @Test
    @DisplayName("should throw UncheckedIOException when the IOLongSupplier fails")
    void shouldThrowUncheckedIOExceptionWhenSupplierFails() {
        // Arrange
        final IOException cause = new IOException(CAUSE_MESSAGE);
        // Use BrokenInputStream as a realistic source of an IOException.
        final IOLongSupplier failingSupplier = () -> new BrokenInputStream(cause).read();

        // Act & Assert
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class,
            () -> Uncheck.getAsLong(failingSupplier, () -> CUSTOM_MESSAGE),
            "An UncheckedIOException should be thrown when the underlying operation fails.");

        assertUncheckedIOException(cause, thrown);
    }
}