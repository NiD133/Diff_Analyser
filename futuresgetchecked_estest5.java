package com.google.common.util.concurrent;

import org.junit.Test;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Tests for {@link FuturesGetChecked}.
 */
public class FuturesGetCheckedTest {

    /**
     * Verifies that the timed {@code getChecked} method throws a NullPointerException
     * when the provided Future is null.
     */
    @Test(expected = NullPointerException.class)
    public void getChecked_withTimeout_givenNullFuture_throwsNullPointerException() throws Exception {
        // Arrange: Define arbitrary parameters for the method call. Their specific values
        // are irrelevant to this test, as the null check on the future happens first.
        Class<Exception> anyExceptionType = Exception.class;
        long anyTimeout = 1L;
        TimeUnit anyUnit = TimeUnit.SECONDS;

        // Act & Assert: Call the method with a null future.
        // The @Test(expected=...) annotation asserts that a NullPointerException is thrown.
        FuturesGetChecked.getChecked((Future<Object>) null, anyExceptionType, anyTimeout, anyUnit);
    }
}