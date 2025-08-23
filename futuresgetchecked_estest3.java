package com.google.common.util.concurrent;

import static org.junit.Assert.assertSame;

import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.junit.Test;

/**
 * Tests for {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    @Test
    public void getChecked_whenFutureCompletesSuccessfully_returnsFuturesResult() throws Exception {
        // Arrange
        // Create a result object. In this case, the successful result of the Future is an
        // instance of SQLException. This tests that getChecked correctly handles the success path,
        // even when the result type is a Throwable.
        SQLException expectedResult = new SQLException("This is a successful result, not a failure cause.");
        Future<SQLException> successfulFuture = CompletableFuture.completedFuture(expectedResult);

        // The exception class parameter is not used when the future succeeds, but is required by the
        // method signature. We can use any valid exception type.
        Class<Exception> exceptionClass = Exception.class;

        // Act
        SQLException actualResult = FuturesGetChecked.getChecked(successfulFuture, exceptionClass);

        // Assert
        // The method should return the future's result without throwing an exception.
        assertSame(
            "getChecked should return the successful result of the future",
            expectedResult,
            actualResult);
    }
}