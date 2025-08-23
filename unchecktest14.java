package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#getAsInt(IOIntSupplier)}.
 * This test class is focused, self-contained, and clearly documents its intent
 * through descriptive naming and structure.
 */
@DisplayName("Uncheck.getAsInt")
class UncheckGetAsIntTest {

    @Test
    @DisplayName("should return the value from the supplier when it succeeds")
    void getAsInt_whenSupplierSucceeds_shouldReturnValue() {
        // Arrange: Create a supplier that successfully returns a value and has a verifiable side effect.
        final AtomicInteger sideEffectState = new AtomicInteger(0);
        final int expectedValue = 1;
        final IOIntSupplier successfulSupplier = () -> {
            sideEffectState.set(expectedValue);
            return expectedValue;
        };

        // Act: Call the method under test.
        final int result = Uncheck.getAsInt(successfulSupplier);

        // Assert: Verify both the return value and the side effect.
        assertEquals(expectedValue, result, "The returned value should match the supplier's result.");
        assertEquals(expectedValue, sideEffectState.get(), "The side effect of the supplier should be observable.");
    }

    @Test
    @DisplayName("should throw UncheckedIOException when the supplier throws an IOException")
    void getAsInt_whenSupplierThrowsIOException_shouldThrowUncheckedIOException() {
        // Arrange: Define the IOException to be thrown and the supplier that throws it.
        final IOException cause = new IOException("Test I/O failure");
        final IOIntSupplier throwingSupplier = () -> {
            throw cause;
        };

        // Act & Assert: Verify that an UncheckedIOException is thrown.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.getAsInt(throwingSupplier);
        }, "Expected Uncheck.getAsInt to throw an UncheckedIOException.");

        // Assert: Verify that the thrown exception has the correct cause.
        assertEquals(cause, thrown.getCause(), "The cause of the UncheckedIOException should be the original IOException.");
    }
}