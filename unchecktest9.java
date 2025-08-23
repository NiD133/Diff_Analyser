package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#apply(IOFunction, Object)}.
 */
class UncheckApplyIOFunctionTest {

    /**
     * Tests that when the provided IOFunction throws an IOException,
     * Uncheck.apply wraps it in an UncheckedIOException.
     */
    @Test
    void testApplyWrapsIOException() {
        // Arrange
        final IOException cause = new IOException("Test Exception");
        final IOFunction<String, String> throwingFunction = t -> {
            throw cause;
        };

        // Act & Assert
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.apply(throwingFunction, "input");
        });

        // Verify that the original IOException is the cause of the UncheckedIOException
        assertEquals(cause, thrown.getCause());
    }

    /**
     * Tests that when the provided IOFunction executes successfully,
     * Uncheck.apply returns the correct value and executes the function's logic.
     */
    @Test
    void testApplyReturnsResultOnSuccess() {
        // Arrange
        final AtomicReference<String> sideEffectCheck = new AtomicReference<>();
        final String input = "input value";
        final String expectedResult = "output value";

        // An IOFunction that performs a side-effect and returns a result.
        // It is declared with 'throws IOException' but does not throw in this case.
        final IOFunction<String, String> successfulFunction = t -> {
            sideEffectCheck.set("processed: " + t);
            return expectedResult;
        };

        // Act
        final String actualResult = Uncheck.apply(successfulFunction, input);

        // Assert
        // 1. The return value should be what the function produced.
        assertEquals(expectedResult, actualResult);
        // 2. The side-effect of the function should have occurred.
        assertEquals("processed: " + input, sideEffectCheck.get());
    }
}