package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#get(IOSupplier)}.
 */
@DisplayName("Uncheck.get(IOSupplier)")
class UncheckGetTest {

    private static final byte[] TEST_BYTES = {'a', 'b'};

    /**
     * Tests that Uncheck.get() returns the value from the supplier when no exception is thrown.
     */
    @Test
    @DisplayName("Should return the supplier's result on success")
    void shouldReturnSuppliersResultOnSuccess() {
        // Arrange: An IOSupplier that successfully reads a byte from an input stream.
        final IOSupplier<Integer> successfulSupplier = () -> new ByteArrayInputStream(TEST_BYTES).read();

        // Act: Call Uncheck.get() with the successful supplier.
        final int result = Uncheck.get(successfulSupplier);

        // Assert: The result should be the first byte from the stream.
        assertEquals('a', result);
    }

    /**
     * Tests that Uncheck.get() wraps an IOException in an UncheckedIOException.
     */
    @Test
    @DisplayName("Should throw UncheckedIOException when the supplier throws an IOException")
    void shouldThrowUncheckedIOExceptionOnFailure() {
        // Arrange: An IOSupplier that is designed to fail by throwing an IOException.
        final IOException cause = new IOException("Test I/O failure");
        final IOSupplier<?> failingSupplier = () -> {
            throw cause;
        };

        // Act & Assert: Verify that calling Uncheck.get() throws an UncheckedIOException
        // and that the original IOException is the cause.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.get(failingSupplier);
        });
        assertSame(cause, thrown.getCause(), "The cause of the UncheckedIOException should be the original IOException.");
    }

    /**
     * Tests that Uncheck.get() correctly executes a supplier that has side effects.
     */
    @Test
    @DisplayName("Should correctly execute a supplier with side effects")
    void shouldExecuteSupplierWithSideEffects() {
        // Arrange: An AtomicReference to observe the side effect and an IOSupplier that modifies it.
        final AtomicReference<String> sideEffectState = new AtomicReference<>("initial");
        final String expectedValue = "updated";
        final IOSupplier<String> supplierWithSideEffect = () -> {
            sideEffectState.set(expectedValue);
            return expectedValue;
        };

        // Act: Call Uncheck.get() with the supplier.
        final String result = Uncheck.get(supplierWithSideEffect);

        // Assert: The returned value and the side effect are both correct.
        assertEquals(expectedValue, result, "The method should return the value from the supplier.");
        assertEquals(expectedValue, sideEffectState.get(), "The supplier's side effect should have been executed.");
    }
}