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
 * Tests for {@link Uncheck#apply(IOTriFunction, Object, Object, Object)}.
 */
@DisplayName("Uncheck.apply(IOTriFunction)")
class UncheckApplyIOTriFunctionTest {

    private AtomicReference<String> arg1Capture;
    private AtomicReference<String> arg2Capture;
    private AtomicReference<String> arg3Capture;

    @BeforeEach
    void setUp() {
        arg1Capture = new AtomicReference<>();
        arg2Capture = new AtomicReference<>();
        arg3Capture = new AtomicReference<>();
    }

    @Test
    void shouldWrapIOExceptionInUncheckedIOException() {
        // Arrange
        final IOException cause = new IOException("test exception");
        final IOTriFunction<Object, Object, Object, ?> throwingFunction = (t, u, v) -> {
            throw cause;
        };

        // Act & Assert
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.apply(throwingFunction, "arg1", "arg2", "arg3");
        });

        // Assert that the original IOException is the cause
        assertEquals(cause, thrown.getCause());
    }

    @Test
    void shouldReturnResultAndPassArgumentsCorrectly() throws IOException {
        // Arrange
        final String input1 = "Value1";
        final String input2 = "Value2";
        final String input3 = "Value3";
        final String expectedResult = "Result";

        // A function that captures its arguments and returns a predictable result.
        final IOTriFunction<String, String, String, String> function = (t, u, v) -> {
            arg1Capture.set(t);
            arg2Capture.set(u);
            arg3Capture.set(v);
            return expectedResult;
        };

        // Act
        final String actualResult = Uncheck.apply(function, input1, input2, input3);

        // Assert
        assertEquals(expectedResult, actualResult, "The return value should match the expected result.");
        assertEquals(input1, arg1Capture.get(), "The first argument was not captured correctly.");
        assertEquals(input2, arg2Capture.get(), "The second argument was not captured correctly.");
        assertEquals(input3, arg3Capture.get(), "The third argument was not captured correctly.");
    }
}