package org.apache.commons.io.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.io.function.IOBiFunction;
import org.apache.commons.io.function.Uncheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Uncheck#apply(IOBiFunction, Object, Object)}.
 */
class UncheckApplyIOBiFunctionTest {

    @Test
    @DisplayName("Should wrap IOException in UncheckedIOException when the function throws")
    void testApplyWrapsIOException() {
        // Arrange
        final String exceptionMessage = "Test I/O failure";
        final IOBiFunction<String, String, String> throwingFunction = (t, u) -> {
            throw new IOException(exceptionMessage);
        };

        // Act & Assert
        final UncheckedIOException thrown = assertThrows(UncheckedIOException.class, () -> {
            Uncheck.apply(throwingFunction, "input1", "input2");
        });

        // Assert that the cause is the original IOException
        final Throwable cause = thrown.getCause();
        assertInstanceOf(IOException.class, cause, "The cause should be an IOException");
        assertEquals(exceptionMessage, cause.getMessage(), "The cause message should match the original exception's message");
    }

    @Nested
    @DisplayName("When the function executes successfully")
    class SuccessPathTest {

        private AtomicReference<String> arg1Capture;
        private AtomicReference<String> arg2Capture;

        @BeforeEach
        void setUp() {
            arg1Capture = new AtomicReference<>();
            arg2Capture = new AtomicReference<>();
        }

        @Test
        @DisplayName("Should return the function's result and pass arguments correctly")
        void testApplyReturnsResultAndPassesArguments() {
            // Arrange
            final String input1 = "Hello";
            final String input2 = "World";
            final String expectedResult = "HelloWorld";

            // A function that captures its arguments and returns a concatenated string.
            final IOBiFunction<String, String, String> function = (t, u) -> {
                arg1Capture.set(t);
                arg2Capture.set(u);
                return t + u;
            };

            // Act
            final String actualResult = Uncheck.apply(function, input1, input2);

            // Assert
            assertEquals(expectedResult, actualResult, "The returned value should match the function's output");
            assertEquals(input1, arg1Capture.get(), "The first argument should be passed correctly to the function");
            assertEquals(input2, arg2Capture.get(), "The second argument should be passed correctly to the function");
        }
    }
}