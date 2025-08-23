package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#getAsInt(IOIntSupplier, Supplier)}.
 */
public class UncheckTestTest15 {

    private static final String CAUSE_MESSAGE = "CauseMessage";
    private static final String CUSTOM_MESSAGE = "Custom message";

    /**
     * A helper assertion to verify the properties of the thrown UncheckedIOException.
     * It checks the custom message and the details of the wrapped IOException cause.
     */
    private void assertUncheckedIOException(final IOException expectedCause, final UncheckedIOException actualException) {
        assertEquals(CUSTOM_MESSAGE, actualException.getMessage(), "The custom message should match.");
        final IOException actualCause = actualException.getCause();
        assertEquals(expectedCause.getClass(), actualCause.getClass(), "The cause's type should match the original IOException.");
        assertEquals(CAUSE_MESSAGE, actualCause.getMessage(), "The cause's message should match.");
    }

    /**
     * Tests that when the IOIntSupplier executes successfully,
     * {@code Uncheck.getAsInt()} returns the expected integer value.
     */
    @Test
    void getAsIntWithMessage_whenSupplierSucceeds_returnsValue() {
        // Arrange
        final AtomicInteger sideEffectCheck = new AtomicInteger(0);
        final IOIntSupplier successfulSupplier = () -> {
            sideEffectCheck.set(1);
            return 1;
        };

        // Act
        final int result = Uncheck.getAsInt(successfulSupplier, () -> CUSTOM_MESSAGE);

        // Assert
        assertEquals(1, result, "Should return the value from the supplier.");
        assertEquals(1, sideEffectCheck.get(), "The supplier's side effect should be observable.");
    }

    /**
     * Tests that when the IOIntSupplier throws an IOException,
     * {@code Uncheck.getAsInt()} wraps it in an UncheckedIOException with the custom message.
     */
    @Test
    void getAsIntWithMessage_whenSupplierFails_throwsUncheckedIOException() {
        // Arrange
        final IOException ioException = new IOException(CAUSE_MESSAGE);
        final IOIntSupplier failingSupplier = () -> {
            throw ioException;
        };

        // Act & Assert
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.getAsInt(failingSupplier, () -> CUSTOM_MESSAGE);
        }, "An UncheckedIOException should be thrown when the supplier fails.");

        // Verify the details of the thrown exception
        assertUncheckedIOException(ioException, thrown);
    }
}