package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#getAsLong(IOLongSupplier)}.
 */
@DisplayName("Uncheck.getAsLong")
class UncheckGetAsLongTest {

    @Test
    @DisplayName("should return the value from the supplier on success")
    void shouldReturnSuppliedValueOnSuccess() {
        // Arrange: A simple supplier that returns a known value.
        final long expectedValue = 42L;
        final IOLongSupplier supplier = () -> expectedValue;

        // Act: Call the method under test.
        final long actualValue = Uncheck.getAsLong(supplier);

        // Assert: The returned value matches the expected value.
        assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("should throw UncheckedIOException when the supplier throws an IOException")
    void shouldWrapIOExceptionInUncheckedIOException() {
        // Arrange: A supplier that is guaranteed to throw an IOException.
        final IOException cause = new IOException("test failure");
        final IOLongSupplier throwingSupplier = () -> {
            throw cause;
        };

        // Act & Assert: Call the method and verify that it throws the expected wrapper exception.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.getAsLong(throwingSupplier);
        });

        // Assert: Verify that the cause of the thrown exception is the original IOException.
        assertEquals(cause, thrown.getCause(), "The original IOException should be the cause of the UncheckedIOException.");
    }
}