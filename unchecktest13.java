package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#getAsBoolean(IOBooleanSupplier)}.
 */
@DisplayName("Uncheck.getAsBoolean")
class UncheckGetAsBooleanTest {

    private static final String TEST_EXCEPTION_MESSAGE = "Test I/O failure";

    private AtomicBoolean sideEffectFlag;

    @BeforeEach
    void setUp() {
        sideEffectFlag = new AtomicBoolean(false);
    }

    @Test
    @DisplayName("should wrap a thrown IOException in an UncheckedIOException")
    void shouldWrapIOException() {
        // Arrange: An IOBooleanSupplier that is guaranteed to throw an IOException.
        final IOBooleanSupplier throwingSupplier = () -> {
            throw new IOException(TEST_EXCEPTION_MESSAGE);
        };

        // Act & Assert: Call the method and verify the expected exception is thrown.
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.getAsBoolean(throwingSupplier);
        });

        // Assert: Verify the cause of the thrown exception is the original IOException.
        final Throwable cause = thrown.getCause();
        assertInstanceOf(IOException.class, cause, "The cause should be an IOException.");
        assertEquals(TEST_EXCEPTION_MESSAGE, cause.getMessage(), "The cause's message should match the original exception's message.");
    }

    @Test
    @DisplayName("should return the correct boolean value and execute side effects on success")
    void shouldReturnValueAndExecuteSideEffectOnSuccess() {
        // Arrange: An IOBooleanSupplier that performs a side effect and returns true.
        // This supplier is declared to throw IOException but does not in this case.
        final IOBooleanSupplier successfulSupplier = () -> {
            sideEffectFlag.set(true);
            return true;
        };

        // Act: Call the method under test.
        final boolean result = Uncheck.getAsBoolean(successfulSupplier);

        // Assert: Verify the return value and the side effect.
        assertTrue(result, "The method should return the value from the supplier.");
        assertTrue(sideEffectFlag.get(), "The side effect from the supplier should have been executed.");
    }
}