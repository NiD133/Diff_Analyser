package com.google.common.util.concurrent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;

/**
 * Understandable tests for {@link FuturesGetChecked}.
 *
 * This class provides clear, maintainable tests for the functionality of the
 * FuturesGetChecked utility, focusing on readability and developer comprehension.
 */
class FuturesGetCheckedTest {

    // A helper exception class for testing.
    // It's a checked exception with a constructor that accepts a Throwable,
    // which is required by Futures.getChecked.
    private static class TestCheckedException extends Exception {
        public TestCheckedException(String message) {
            super(message);
        }

        public TestCheckedException(Throwable cause) {
            super(cause);
        }
    }

    // A helper exception class with no constructor that getChecked can use.
    private static class NoValidConstructorException extends Exception {
        public NoValidConstructorException(int value) {
            super(String.valueOf(value));
        }
    }

    @Test
    void getChecked_futureCompletesWithValue_returnsValue() throws Exception {
        // Arrange
        Future<String> completedFuture = CompletableFuture.completedFuture("success");

        // Act
        String result = FuturesGetChecked.getChecked(completedFuture, TestCheckedException.class);

        // Assert
        assertEquals("success", result);
    }

    @Test
    void getChecked_futureCompletesWithNull_returnsNull() throws Exception {
        // Arrange
        Future<Object> completedFutureWithNull = CompletableFuture.completedFuture(null);

        // Act
        Object result = FuturesGetChecked.getChecked(completedFutureWithNull, TestCheckedException.class);

        // Assert
        assertNull(result);
    }

    @Test
    void getChecked_futureFails_throwsCheckedException() {
        // Arrange
        SQLException cause = new SQLException("Database connection failed");
        Future<String> failedFuture = CompletableFuture.failedFuture(cause);

        // Act & Assert
        // The thrown exception should be of the specified type, with the original exception as its cause.
        IOException thrown = assertThrows(IOException.class, () -> {
            FuturesGetChecked.getChecked(failedFuture, IOException.class);
        });
        assertEquals(cause, thrown.getCause());
    }

    @Test
    void getCheckedWithTimeout_futureCompletesWithValue_returnsValue() throws Exception {
        // Arrange
        Future<String> completedFuture = CompletableFuture.completedFuture("success");

        // Act
        String result = FuturesGetChecked.getChecked(completedFuture, TestCheckedException.class, 1, TimeUnit.SECONDS);

        // Assert
        assertEquals("success", result);
    }

    @Test
    void getCheckedWithTimeout_futureTimesOut_throwsCheckedExceptionWrappingTimeoutException() {
        // Arrange
        // A future that will never complete, to simulate a timeout.
        Future<String> nonCompletingFuture = new CompletableFuture<>();

        // Act & Assert
        // We expect getChecked to throw TestCheckedException, caused by a TimeoutException.
        TestCheckedException thrown = assertThrows(TestCheckedException.class, () -> {
            FuturesGetChecked.getChecked(nonCompletingFuture, TestCheckedException.class, 10, TimeUnit.MILLISECONDS);
        });

        assertInstanceOf(TimeoutException.class, thrown.getCause());
    }

    @Test
    void getChecked_withNullFuture_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            FuturesGetChecked.getChecked(null, TestCheckedException.class);
        });
    }

    @Test
    void getCheckedWithTimeout_withNullFuture_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            FuturesGetChecked.getChecked(null, TestCheckedException.class, 1, TimeUnit.SECONDS);
        });
    }

    @Test
    void isCheckedException_withCheckedException_returnsTrue() {
        // isCheckedException should return true for standard checked exceptions.
        assertTrue(FuturesGetChecked.isCheckedException(Exception.class));
        assertTrue(FuturesGetChecked.isCheckedException(IOException.class));
    }

    @Test
    void isCheckedException_withUncheckedException_returnsFalse() {
        // isCheckedException should return false for runtime exceptions and errors.
        // Note: The original tests did not cover this case.
        assertTrue(!FuturesGetChecked.isCheckedException(RuntimeException.class));
        assertTrue(!FuturesGetChecked.isCheckedException(Error.class));
    }

    @Test
    void checkExceptionClassValidity_withValidExceptionType_doesNotThrow() {
        // A valid exception class is a checked exception with a constructor
        // that can be used by getChecked (e.g., takes a Throwable or String).
        assertDoesNotThrow(() -> FuturesGetChecked.checkExceptionClassValidity(IOException.class));
        assertDoesNotThrow(() -> FuturesGetChecked.checkExceptionClassValidity(TestCheckedException.class));
    }

    @Test
    void checkExceptionClassValidity_withRuntimeException_throwsIllegalArgumentException() {
        // RuntimeException is not a checked exception, so it's invalid.
        assertThrows(IllegalArgumentException.class, () -> {
            FuturesGetChecked.checkExceptionClassValidity(RuntimeException.class);
        });
    }

    @Test
    void checkExceptionClassValidity_withNoValidConstructor_throwsIllegalArgumentException() {
        // This checked exception has no constructor usable by getChecked.
        assertThrows(IllegalArgumentException.class, () -> {
            FuturesGetChecked.checkExceptionClassValidity(NoValidConstructorException.class);
        });
    }

    @Test
    void checkExceptionClassValidity_withNullClass_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            FuturesGetChecked.checkExceptionClassValidity(null);
        });
    }

    @Test
    void validatorFactories_returnNonNullValidators() {
        // The factory methods for validators should return non-null instances.
        assertNotNull(FuturesGetChecked.weakSetValidator());
        assertNotNull(FuturesGetChecked.classValueValidator());
        assertNotNull(FuturesGetChecked.GetCheckedTypeValidatorHolder.getBestValidator());
    }
}