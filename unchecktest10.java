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
 * Tests for {@link Uncheck#apply(IOQuadFunction, Object, Object, Object, Object)}.
 */
class UncheckApplyQuadFunctionTest {

    // AtomicReferences to capture the arguments passed to the lambda.
    private AtomicReference<String> capturedArg1;
    private AtomicReference<String> capturedArg2;
    private AtomicReference<String> capturedArg3;
    private AtomicReference<String> capturedArg4;

    @BeforeEach
    void setUp() {
        capturedArg1 = new AtomicReference<>();
        capturedArg2 = new AtomicReference<>();
        capturedArg3 = new AtomicReference<>();
        capturedArg4 = new AtomicReference<>();
    }

    @Test
    @DisplayName("apply() with a quad-function should return the result and pass arguments correctly")
    void applyWithQuadFunctionShouldWorkCorrectly() {
        // Arrange
        final String arg1 = "Apple";
        final String arg2 = "Banana";
        final String arg3 = "Cherry";
        final String arg4 = "Date";
        final String expectedResult = "Fruit Salad";

        // A function that captures its arguments and returns a fixed result.
        final IOQuadFunction<String, String, String, String, String> function = (t, u, v, w) -> {
            capturedArg1.set(t);
            capturedArg2.set(u);
            capturedArg3.set(v);
            capturedArg4.set(w);
            return expectedResult;
        };

        // Act
        final String actualResult = Uncheck.apply(function, arg1, arg2, arg3, arg4);

        // Assert
        assertEquals(expectedResult, actualResult, "The return value should match the expected result.");
        assertEquals(arg1, capturedArg1.get(), "The first argument should be passed correctly.");
        assertEquals(arg2, capturedArg2.get(), "The second argument should be passed correctly.");
        assertEquals(arg3, capturedArg3.get(), "The third argument should be passed correctly.");
        assertEquals(arg4, capturedArg4.get(), "The fourth argument should be passed correctly.");
    }

    @Test
    @DisplayName("apply() with a quad-function that throws IOException should rethrow it as UncheckedIOException")
    void applyWithThrowingQuadFunctionShouldThrowUncheckedIOException() {
        // Arrange: A quad-function that always throws an IOException.
        final IOQuadFunction<Object, Object, Object, Object, ?> throwingFunction = (t, u, v, w) -> {
            throw new IOException("Test I/O failure");
        };

        // Act & Assert
        // The arguments can be null as they are not used before the exception is thrown.
        assertThrows(UncheckedIOException.class, () -> {
            Uncheck.apply(throwingFunction, null, null, null, null);
        });
    }
}